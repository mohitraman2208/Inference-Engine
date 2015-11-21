package com.ai.homework3.io;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;

import com.ai.inference.InferenceEngine;

public class IOHandler {

	public boolean readInputAndInitEngine(String inputFileName, InferenceEngine inferenceEngine) 
	{
		boolean retval = false;

		File inputFile = new File(inputFileName);

		if(!inputFile.exists())
		{
			System.out.print("******** Error Opening File!***********\n*************File Does not exist!************\n");
		}
		else
		{
			BufferedReader readObj = null; 
			try 
			{
				readObj = new BufferedReader(new FileReader(inputFile));
				
				String query = readObj.readLine().trim();
				
				inferenceEngine.setQuery(query);
				
				int counter = Integer.parseInt(readObj.readLine().trim());
				
				String sentence;
				while( (counter-- > 0) && (sentence = readObj.readLine())!=null)
				{
					inferenceEngine.addSentenceToKB(sentence.trim());
				}

				readObj.close();

				retval = true;
			}
			catch(Exception e)
			{
				e.printStackTrace();
			}
		}

		return retval;
	}

	public boolean writeToFile(String outputfilename, Boolean result ) 
	{
		boolean retval = false;
		try
		{
			File myFile = new File(outputfilename);
			if(!myFile.exists())
			{
				myFile.createNewFile();
			}

			BufferedWriter writer = new BufferedWriter(new FileWriter(myFile));
			writer.write(result.toString().toUpperCase());

			writer.close();
			
			retval = true;
		}catch(Exception e)
		{
			e.printStackTrace();
		}
		return retval;
	}
}