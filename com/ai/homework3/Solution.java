package com.ai.homework3;

import com.ai.homework3.io.IOHandler;
import com.ai.inference.InferenceEngine;


public class Solution 
{
	public static final String inputFileName = "input.txt";

	public static final String outputFileName = "output.txt";

	public static void main(String[] args)
	{
		try 
		{
			boolean retval = false;

			IOHandler ioHandler = new IOHandler();
			InferenceEngine inferenceEngine = new InferenceEngine();
			
			retval = ioHandler.readInputAndInitEngine(inputFileName,inferenceEngine);
			if(retval)
			{
				boolean isQueryInfered = inferenceEngine.inferBackwardChaining();
				retval = ioHandler.writeToFile(outputFileName, isQueryInfered);
			}
			else
			{
				//Error during I/P
			}
			
		}
		catch(Exception e)
		{
			e.printStackTrace();
		}
	}
}