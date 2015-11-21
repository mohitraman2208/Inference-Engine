package com.ai.inference.kb;

import java.util.ArrayList;
import java.util.List;

public class PredicateTable 
{
	private List<Sentence> sentences;
	
	private List<AtomicSentence> constants;

	public PredicateTable()
	{
		this.sentences = new ArrayList<Sentence>();
		this.constants = new ArrayList<AtomicSentence>();
	}
	
	public List<Sentence> getSentences() {
		return sentences;
	}

	public void setSentences(List<Sentence> sentences)
	{
		this.sentences = sentences;
	}

	public List<AtomicSentence> getConstants() 
	{
		return constants;
	}

	public void setConstants(List<AtomicSentence> constants) 
	{
		this.constants = constants;
	}

	public void addSentence(Sentence sentence) 
	{
		if(sentence.isAtomicSentence())
		{
			constants.add(sentence.getConclusion());
		}
		else
		{
			sentences.add(sentence);
		}
	}

	public boolean searchFacts(AtomicSentence querySentence) 
	{
		for(AtomicSentence fact: constants)
		{
			if(fact.equals(querySentence))
				return true;
		}
		
		return false;
	}
}
