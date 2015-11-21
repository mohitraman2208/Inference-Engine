package com.ai.inference.kb;

import java.util.ArrayList;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class AtomicSentence 
{
	private String predicate;
	
	private List<String> arguments;
	
	private boolean isConstantSentence;
	
	private int numOfArgs;

	public AtomicSentence(String sentenceStr) 
	{
		this();
		createAtomicStence(sentenceStr);
	}
	
	public AtomicSentence(AtomicSentence atomicSentence) 
	{
		this();
		
		for(String argument : atomicSentence.getArguments())
		{
			this.arguments.add(argument);
		}
		
		this.predicate = atomicSentence.getPredicate();
		this.isConstantSentence =  atomicSentence.isConstantSentence;
		this.numOfArgs = atomicSentence.numOfArgs;
	}
	
	public AtomicSentence()
	{
		predicate = "";
		arguments = new ArrayList<String>();
		numOfArgs = 0;
		this.isConstantSentence = true;
	}

	private void createAtomicStence(String sentenceStr) 
	{
		
		Pattern pattern = Pattern.compile("\\s*([\\w]+)\\s*\\(\\s*([\\w,\\s]+)\\s*\\)\\s*");

		Matcher matcher = 
				pattern.matcher(sentenceStr);
		
		if(matcher.find() && matcher.groupCount() == 2)
		{
			this.predicate = matcher.group(1);
			
			String[] arguments = matcher.group(2).split(",");
			for(String argString : arguments)
			{
				if(this.isConstantSentence && isVariable(argString))
				{
					this.isConstantSentence = false;
				}
				this.arguments.add(argString.trim());
				++(this.numOfArgs);
			}
		}
		else
		{
			// TODO Error in I/P
			throw new RuntimeException();
		}

	}
	
	public static boolean isVariable(String arg)
	{
		return Character.isLowerCase(arg.trim().charAt(0));
	}
	
	public String getPredicate() {
		return predicate;
	}

	public void setPredicate(String predicate) {
		this.predicate = predicate;
	}

	public List<String> getArguments() {
		return arguments;
	}

	public void setArguments(List<String> arguments) {
		this.arguments = arguments;
	}

	public boolean isConstantSentence() {
		return isConstantSentence;
	}

	public void setConstantSentence(boolean isConstantSentence) {
		this.isConstantSentence = isConstantSentence;
	}

	/**
	 * @return the numOfArgs
	 */
	public int getNumOfArgs() {
		return numOfArgs;
	}

	/**
	 * @param numOfArgs the numOfArgs to set
	 */
	public void setNumOfArgs(int numOfArgs) {
		this.numOfArgs = numOfArgs;
	}
	
	public String key()
	{
		return this.predicate + String.valueOf(this.numOfArgs);
	}
	
	@Override
	public boolean equals(Object arg) 
	{
		boolean retval = true;
		if(arg != null && arg.getClass() == getClass())
		{
			AtomicSentence query = (AtomicSentence)arg;
			if(query.getNumOfArgs() == getNumOfArgs())
			{
				for (int i = 0; i < query.getNumOfArgs(); i++)
				{
					String queryArg = query.getArguments().get(i);
					String currArg = getArguments().get(i);
					if(!queryArg.equals(currArg))
					{
						retval = false;
						break;
					}
				}
			}
		}
		else
		{
			retval = false;
		}
		
		return retval;
	}
	
	public static Substitutions getSubstitutions(AtomicSentence variableSentence,AtomicSentence constantSentence)
	{
		Substitutions substitutions = new Substitutions();
		boolean isValidSubstitution = true;
		if(variableSentence.predicate.equals(constantSentence.predicate) && variableSentence.numOfArgs == constantSentence.numOfArgs)
		{
			
			for(int i = 0 ; i < variableSentence.numOfArgs;i++)
			{
				String argKB = variableSentence.getArguments().get(i);
				
				if(isVariable(argKB))
				{
					if(substitutions.hasVariable(argKB))
					{
						if(!substitutions.getVariableToValue().get(argKB).equals(constantSentence.getArguments().get(i)))
						{
							isValidSubstitution = false;
							break;
						}
						
					}
					else
					{
						substitutions.addSubstitution(argKB, constantSentence.getArguments().get(i));
					}
				}
			}
		}
		
		if(isValidSubstitution)
			return substitutions;
		return null;
	}
	
	
	public static Substitutions getBothSubstitutions(AtomicSentence variableSentence,AtomicSentence variableSentenceTwo)
	{
		Substitutions substitutions = new Substitutions();
		boolean isValidSubstitution = true;
		if(variableSentence.predicate.equals(variableSentenceTwo.predicate) && variableSentence.numOfArgs == variableSentenceTwo.numOfArgs)
		{
			
			for(int i = 0 ; i < variableSentence.numOfArgs;i++)
			{
				String argKB = variableSentence.getArguments().get(i);
				
				if(isVariable(argKB))
				{
					if(substitutions.hasVariable(argKB))
					{
						if(!substitutions.getVariableToValue().get(argKB).equals(variableSentenceTwo.getArguments().get(i)))
						{
							isValidSubstitution = false;
							break;
						}
						
					}
					else
					{
						substitutions.addSubstitution(argKB, variableSentenceTwo.getArguments().get(i));
					}
				}
			}
		}
		
		if(isValidSubstitution)
			return substitutions;
		return null;
	}
	@Override
	public String toString() {
		
		StringBuffer argBuff = new StringBuffer();
		for(String args :this.getArguments())
		{
			argBuff.append(args).append(",");
		}
		return new StringBuffer().append(this.predicate).append("(").append(argBuff).append(")").toString();
	}
}
