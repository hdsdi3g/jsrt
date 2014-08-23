/*
 * This file is part of hdsdi3g jsrt fork.
 * 
 * This library is free software; you can redistribute it and/or modify
 * it under the terms of the GNU Lesser General Public License as published by
 * the Free Software Foundation; either version 3 of the License, or
 * any later version.
 *
 * This library is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU Lesser General Public License for more details.
 * 
 * Copyright (C) hdsdi3g for hd3g.tv 2014
 * 
*/
package hd3gtv.jsrt;

import hd3gtv.tools.ApplicationArgs;

import java.io.File;
import java.io.PrintWriter;
import java.nio.charset.Charset;

import org.fredy.jsrt.api.SRT;
import org.fredy.jsrt.api.SRTInfo;
import org.fredy.jsrt.api.SRTReader;
import org.fredy.jsrt.api.SRTTimeFormat;
import org.fredy.jsrt.api.SRTWriter;

public class MainClass {
	
	public static void main(String[] args) {
		try {
			ApplicationArgs aargs = new ApplicationArgs(args);
			
			Charset source_charset = Charset.forName("UTF-8");
			if (aargs.getParamExist("-iso")) {
				source_charset = Charset.forName("ISO-8859-1");
			}
			
			float rate_factor = 1f;
			if (aargs.getParamExist("-ifps") | aargs.getParamExist("-ofps")) {
				float ifps = Float.valueOf(aargs.getSimpleParamValue("-ifps"));
				float ofps = Float.valueOf(aargs.getSimpleParamValue("-ofps"));
				rate_factor = ofps / ifps;
			}
			
			float offset = 0f;
			if (aargs.getParamExist("-offset")) {
				offset = Float.valueOf(aargs.getSimpleParamValue("-offset"));
			}
			
			SRTInfo source = SRTReader.read(new File(aargs.getSimpleParamValue("-i")), source_charset);
			
			if (aargs.getParamExist("-first") & aargs.getParamExist("-last")) {
				float real_first_sub = SRTTimeFormat.parse(aargs.getSimpleParamValue("-first"));
				float real_last_sub = SRTTimeFormat.parse(aargs.getSimpleParamValue("-last"));
				float file_first_sub = source.getFirst().startTime;
				float file_last_sub = source.getLast().startTime;
				
				System.out.println("First subtitle time in file:\t" + file_first_sub + " sec");
				System.out.println("First subtitle time wanted:\t" + real_first_sub + " sec");
				System.out.println("Last subtitle time in file:\t" + file_last_sub + " sec");
				System.out.println("Last subtitle time wanted:\t" + real_last_sub + " sec");
				System.out.println();
				
				offset = real_first_sub - file_first_sub;
				file_last_sub = file_last_sub + offset;
				rate_factor = real_last_sub / file_last_sub;
			}
			
			if (offset != 0f) {
				System.out.println("Offset to apply:\t\t\t\t" + offset + " sec");
			}
			if (rate_factor != 1f) {
				System.out.println("Rate factor to apply for each subtitle:\t\t" + rate_factor);
			}
			
			for (SRT srt : source) {
				srt.updateTiming(offset, rate_factor);
			}
			
			if (aargs.getParamExist("-o")) {
				SRTWriter.write(new File(aargs.getSimpleParamValue("-o")), Charset.forName("UTF-8"), source);
			} else {
				SRTWriter.write(new PrintWriter(System.out), source);
			}
			
		} catch (Exception e) {
			e.printStackTrace();
			System.err.println();
			System.err.println("Usage: -i <inputfile.srt> [-o <outputfile.srt>] [-iso] [-ifps n -ofps n] [-offset n]");
			System.err.println("Or:    -i <inputfile.srt> [-o <outputfile.srt>] [-iso] [-first t -last t]");
			System.err.println(" -iso force to interpret source file in ISO-8859-1 encoding");
			System.err.println(" -ifps n (float in frames per second) input fps source");
			System.err.println(" -ofps n (float in frames per second) output fps for destination");
			System.err.println(" -offset n (float in second) set an temporal offset for all subtitles");
			System.err.println(" -first t (time HH:MM:SS,MSEC) set the real time for the first subtitle");
			System.err.println(" -last t (time HH:MM:SS,MSEC) set the real time for the last subtitle");
		}
	}
}
