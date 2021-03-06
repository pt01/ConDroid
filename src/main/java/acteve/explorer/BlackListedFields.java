/*
  Copyright (c) 2011,2012, 
   Saswat Anand (saswat@gatech.edu)
   Mayur Naik  (naik@cc.gatech.edu)
  All rights reserved.
  
  Redistribution and use in source and binary forms, with or without
  modification, are permitted provided that the following conditions are met: 
  
  1. Redistributions of source code must retain the above copyright notice, this
  list of conditions and the following disclaimer. 
  2. Redistributions in binary form must reproduce the above copyright notice,
  this list of conditions and the following disclaimer in the documentation
  and/or other materials provided with the distribution. 
  
  THIS SOFTWARE IS PROVIDED BY THE COPYRIGHT HOLDERS AND CONTRIBUTORS "AS IS" AND
  ANY EXPRESS OR IMPLIED WARRANTIES, INCLUDING, BUT NOT LIMITED TO, THE IMPLIED
  WARRANTIES OF MERCHANTABILITY AND FITNESS FOR A PARTICULAR PURPOSE ARE
  DISCLAIMED. IN NO EVENT SHALL THE COPYRIGHT OWNER OR CONTRIBUTORS BE LIABLE FOR
  ANY DIRECT, INDIRECT, INCIDENTAL, SPECIAL, EXEMPLARY, OR CONSEQUENTIAL DAMAGES
  (INCLUDING, BUT NOT LIMITED TO, PROCUREMENT OF SUBSTITUTE GOODS OR SERVICES;
  LOSS OF USE, DATA, OR PROFITS; OR BUSINESS INTERRUPTION) HOWEVER CAUSED AND
  ON ANY THEORY OF LIABILITY, WHETHER IN CONTRACT, STRICT LIABILITY, OR TORT
  (INCLUDING NEGLIGENCE OR OTHERWISE) ARISING IN ANY WAY OUT OF THE USE OF THIS
  SOFTWARE, EVEN IF ADVISED OF THE POSSIBILITY OF SUCH DAMAGE.
  
  The views and conclusions contained in the software and documentation are those
  of the authors and should not be interpreted as representing official policies, 
  either expressed or implied, of the FreeBSD Project.
*/

package acteve.explorer;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.util.ArrayList;
import java.util.List;
import java.util.regex.Pattern;

public class BlackListedFields
{
	private static List<String> fieldSigs;
	private static Pattern filter;

	static void setup(String fieldSigsFile, String blackListedFieldsFile)
	{
		if(blackListedFieldsFile == null)
			return;
		fieldSigs = readFileToList(fieldSigsFile);
		filter = readFilter(blackListedFieldsFile);
	}

	static boolean check(int fieldSigId)
	{
		if(filter == null)
			return false;

		if (fieldSigId == -1) 
			return false;

		String fld = fieldSigs.get(fieldSigId);
		if (filter.matcher(fld).matches())
			return true;
		
		return false;
	}

	private static Pattern readFilter(String filterFile) {
		if(!(new File(filterFile).exists()))
			return null;
		List<String> filters = readFileToList(filterFile);
		if(filters.size() == 0)
			return null;
		else if(filters.size() == 1){
			String f = "<"+filters.get(0)+">";
			System.out.println(f);
			return Pattern.compile(f);
		}
		String f = "(<"+filters.remove(0)+">)";
		StringBuilder builder = new StringBuilder(f);
		for(String g : filters) {
			if(g.trim().equals(""))
				continue;
			builder.append("|(<"+g+">)");
		}
		return Pattern.compile(builder.toString());
	}

	private static List<String> readFileToList(String fileName) {
		List<String> list = new ArrayList<String>();
		try {
            BufferedReader in = new BufferedReader(new FileReader(fileName));
            String s;
            while ((s = in.readLine()) != null) {
                list.add(s);
            }
            in.close();
        } catch (Exception ex) {
            throw new RuntimeException(ex);
        }
		return list;
	}
}