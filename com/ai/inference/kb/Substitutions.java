package com.ai.inference.kb;

import java.util.HashMap;
import java.util.Map.Entry;

public class Substitutions
{
	private HashMap<String, String> variableToValue;

	public Substitutions() 
	{
		variableToValue = new HashMap<String, String>();
	}
	
	public int size()
	{
		return variableToValue.size();
	}
	public void addSubstitution(String variable,String value)
	{
		variableToValue.put(variable, value);
	}
	
	public boolean hasVariable(String variable)
	{
		return variableToValue.containsKey(variable);
	}
	
	/**
	 * @return the variableToValue
	 */
	public HashMap<String, String> getVariableToValue() {
		return variableToValue;
	}

	/**
	 * @param variableToValue the variableToValue to set
	 */
	public void setVariableToValue(HashMap<String, String> variableToValue) {
		this.variableToValue = variableToValue;
	}

	public Sentence performSubstitutions(Sentence sentence) 
	{
		Sentence unifiedSentence = new Sentence();
		
		AtomicSentence unifiedConclusion = performSubstitution(sentence.getConclusion());
		unifiedSentence.setConclusion(unifiedConclusion);
		for(AtomicSentence premise : sentence.getPremises())
		{
			unifiedSentence.getPremises().add(performSubstitution(premise));
		}
		return unifiedSentence;
	}
	
	public AtomicSentence performSubstitution(AtomicSentence atom)
	{
		AtomicSentence newAtomicSentence = new AtomicSentence(atom);
		if(atom.isConstantSentence())
			return newAtomicSentence;
		for(int i = 0;i < atom.getNumOfArgs();i++)
		{
			String argString = atom.getArguments().get(i);
			if(AtomicSentence.isVariable(argString))
				newAtomicSentence.getArguments().set(i, variableToValue.get(argString));
		}
		newAtomicSentence.setConstantSentence(true);
		return newAtomicSentence;
	}
	
	@Override
	public boolean equals(Object arg) {
		boolean retval =false;
		if(arg != null && arg.getClass() == getClass())
		{
			Substitutions sub = (Substitutions)arg;
			if(sub.size() == size() && sub.getVariableToValue().keySet().equals(variableToValue.keySet()))
			{
				retval = true;
				for(Entry<String,String> entry : variableToValue.entrySet())
				{
					if(!entry.getValue().equals(sub.getVariableToValue().get(entry.getValue())))
					{
						retval = false;
						break;
					}
				}
			}
		}
		return retval;
	}
}
