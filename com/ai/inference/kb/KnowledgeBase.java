package com.ai.inference.kb;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

public class KnowledgeBase
{

	private HashMap<String, PredicateTable> predicateSentencesMap;

	private List<String> constantNames;
	
	public KnowledgeBase() 
	{
		super();
		predicateSentencesMap = new HashMap<String, PredicateTable>();
		constantNames = new ArrayList<String>();
	}
	
	
	public void addSentence(String sentenceStr)
	{
		Sentence sentence = new Sentence(sentenceStr);
		addSentence(sentence);
	}

	public void addSentence(Sentence sentence)
	{
		addToPredicateSentencesMap(sentence);
		fetchConstants(sentence);
	}

	private void fetchConstants(Sentence sentence) 
	{
		sentence.getConstantNames(constantNames);
	}


	private void addToPredicateSentencesMap(Sentence sentence) 
	{
		if (this.predicateSentencesMap.containsKey((sentence.getConclusion().key())))
		{
			PredicateTable pTable = (PredicateTable)this.predicateSentencesMap.get(sentence.getConclusion().key());
			pTable.addSentence(sentence);
		}
		else
		{
			PredicateTable pTable = new PredicateTable();
			pTable.addSentence(sentence);
			this.predicateSentencesMap.put(sentence.getConclusion().key(), pTable);
		}
	}


	public boolean inferThroughFacts(AtomicSentence querySentence) 
	{
		boolean retval = false;
		
		if(this.predicateSentencesMap.containsKey(querySentence.key()))
		{
			PredicateTable pTable = (PredicateTable)this.predicateSentencesMap.get(querySentence.key());
			retval = pTable.searchFacts(querySentence);
		}
		return retval;
	}


	public List<Sentence> unifySentence(AtomicSentence querySentence) 
	{
		List<Sentence> unifiedSentences = new ArrayList<Sentence>();
		
		if(this.predicateSentencesMap.containsKey(querySentence.key()))
		{
			PredicateTable pTable = (PredicateTable)this.predicateSentencesMap.get(querySentence.key());
			List<Sentence> sentences = pTable.getSentences();
			for(Sentence sentence : sentences)
			{
				if(sentence.getConclusion().isConstantSentence() && sentence.getConclusion().equals(querySentence))
				{
					unifiedSentences.add(sentence);
				}
				else
				{
					Sentence validUnifiedSentence = unfiy(sentence,querySentence);
					if(validUnifiedSentence != null)
						unifiedSentences.add(validUnifiedSentence);
				}
			}
		}
		return unifiedSentences;
	}


	private Sentence unfiy(Sentence sentence, AtomicSentence querySentence) 
	{
		
		Substitutions substituions = AtomicSentence.getSubstitutions(sentence.getConclusion(), querySentence);
		
		Sentence validUnifiedSentence = null;
		if(substituions != null && substituions.size() > 0)
		{
			Sentence unifiedsentence = substituions.performSubstitutions(sentence);
			if(unifiedsentence.getConclusion().equals(querySentence))
				validUnifiedSentence = unifiedsentence;
		}
		return validUnifiedSentence;
	}


	public void getSubstitutionsFromFacts(AtomicSentence variableQuery,
			List<Substitutions> retSubstitions) 
	{
		if(this.predicateSentencesMap.containsKey(variableQuery.key()))
		{
			PredicateTable pTable = (PredicateTable)this.predicateSentencesMap.get(variableQuery.key());
			List<AtomicSentence> constants = pTable.getConstants();
			for(AtomicSentence constantAtom : constants)
			{
				Substitutions sub = AtomicSentence.getSubstitutions(variableQuery, constantAtom);
				if( sub != null && !retSubstitions.contains(sub))
				{
					retSubstitions.add(sub);
				}
			}
		} 
	}


	public List<Sentence> getSameConclusionSent(AtomicSentence variableQuery) 
	{
		List<Sentence> returnedSentences = new ArrayList<Sentence>();
		if(this.predicateSentencesMap.containsKey(variableQuery.key()))
		{
			PredicateTable pTable = (PredicateTable)this.predicateSentencesMap.get(variableQuery.key());
			List<Sentence> sentences = pTable.getSentences();
			for(Sentence sentence : sentences)
			{
				if(matchAtomicSentences(sentence.getConclusion(),variableQuery))
				{
					returnedSentences.add(sentence);
				}
			}
		}
		return returnedSentences;
	}


	private boolean matchAtomicSentences(AtomicSentence myAtomSentence,
			AtomicSentence variableQuery) 
	{
		boolean retval = false;
		if(myAtomSentence.getNumOfArgs() == variableQuery.getNumOfArgs())
		{
			retval = true;
			for (int i = 0; i < myAtomSentence.getNumOfArgs(); i++)
			{
				String queryArg = myAtomSentence.getArguments().get(i);
				String currArg = variableQuery.getArguments().get(i);
				if( !AtomicSentence.isVariable(queryArg) && !AtomicSentence.isVariable(currArg) && !queryArg.equals(currArg))
				{
					retval = false;
					break;
				}
			}
		}
		return retval;
	}


}
