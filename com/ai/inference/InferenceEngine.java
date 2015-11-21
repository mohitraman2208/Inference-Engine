package com.ai.inference;

import java.util.ArrayList;
import java.util.List;

import com.ai.inference.kb.AtomicSentence;
import com.ai.inference.kb.KnowledgeBase;
import com.ai.inference.kb.Sentence;
import com.ai.inference.kb.Substitutions;

public class InferenceEngine 
{
	private KnowledgeBase kb;

	private AtomicSentence query;

	public InferenceEngine() 
	{
		kb = new KnowledgeBase();
	}

	public KnowledgeBase getKb() 
	{
		return kb;
	}

	public void setKb(KnowledgeBase kb)
	{
		this.kb = kb;
	}

	public boolean inferBackwardChaining()
	{
		boolean retval = false;

		if( kb == null || query == null)
		{
			return false;
		}

		retval = inferBCRecursive(this.query);

		return retval;
	}

	private boolean inferBCRecursive(AtomicSentence querySentence) 
	{
		if (kb.inferThroughFacts(querySentence))
			return true;

		boolean isSentenceInfered = false;

		List<Sentence> unifiedSentences = kb.unifySentence(querySentence);

		for(Sentence sentence : unifiedSentences)
		{
			isSentenceInfered = true;
			if(Sentence.hasAllConstantPremises(sentence))
			{
				isSentenceInfered = inferAllPremises(sentence);
			}
			else
			{
				isSentenceInfered = inferWithSubstitutions(sentence);
			}
			if(isSentenceInfered)
			{
				break;
			}
		}

		if(isSentenceInfered)
		{
			Sentence inferedSentence = new Sentence();
			inferedSentence.setConclusion(querySentence);
			inferedSentence.setAtomicSentence(true);
			kb.addSentence(inferedSentence);
		}

		return isSentenceInfered;
	}

	private boolean inferWithSubstitutions(Sentence sentence) 
	{
		boolean isSentenceInfered = true;
		List<AtomicSentence> constantPremises = getConstantPremises(sentence);
		for(AtomicSentence premise : constantPremises)
		{
			isSentenceInfered = inferBCRecursive(premise);
			if(!isSentenceInfered)
			{
				break;
			}

		}
		if(isSentenceInfered)
		{
			List<AtomicSentence> variablePremises = getVariablePremises(sentence);
			AtomicSentence firstPremise = variablePremises.get(0);
			List<Substitutions> allSubstitutions = inferBCSubsRecursive(firstPremise);
			if(allSubstitutions == null || allSubstitutions.size() ==0)
			{
				isSentenceInfered = false;
			}
			else
			{
				for(Substitutions sub : allSubstitutions)
				{
					Sentence unifiedSentence = sub.performSubstitutions(sentence);
					isSentenceInfered = inferAllPremises(unifiedSentence);
				}
			}

		}
		return isSentenceInfered;
	}

	public List<AtomicSentence> getVariablePremises(Sentence sentence) {
		List<AtomicSentence> variablePremises = new ArrayList<AtomicSentence>();
		for(AtomicSentence premise : sentence.getPremises())
		{
			if(!premise.isConstantSentence())
			{
				variablePremises.add(premise);
			}
		}
		return variablePremises;
	}	

	public List<Substitutions> inferBCSubsRecursive(AtomicSentence variableQuery) 
	{
		List<Substitutions> retSubstitions = new ArrayList<Substitutions>();
		kb.getSubstitutionsFromFacts(variableQuery,retSubstitions);

		List<Sentence> sentences = kb.getSameConclusionSent(variableQuery);
		for(Sentence sentence : sentences)
		{
			if(sentence.getConclusion().isConstantSentence())
			{
				boolean isConclusionInfered = inferBCRecursive(sentence.getConclusion());
				if(isConclusionInfered)
				{
					Substitutions sub = AtomicSentence.getSubstitutions(variableQuery, sentence.getConclusion());
					if(sub != null && sub.size()>0)
						retSubstitions.add(sub);
				}
			}
			else
			{
				List<AtomicSentence> constantPremises = getConstantPremises(sentence);
				boolean isSentenceInfered = true;
				for(AtomicSentence premise : constantPremises)
				{
					isSentenceInfered = inferBCRecursive(premise);
					if(!isSentenceInfered)
					{
						break;
					}

				}
				if(isSentenceInfered)
				{
					List<AtomicSentence> variablePremises = getVariablePremises(sentence);
					AtomicSentence firstPremise = variablePremises.get(0);
					List<Substitutions> allSubstitutions = inferBCSubsRecursive(firstPremise);
					if(allSubstitutions != null && allSubstitutions.size() > 0)
					{
						for(Substitutions sub : allSubstitutions)
						{
							Sentence unifiedSentence = sub.performSubstitutions(sentence);
							if(unifiedSentence.getConclusion().equals(sub.performSubstitution(variableQuery)))
							{
								isSentenceInfered = inferAllPremises(unifiedSentence);
								if(isSentenceInfered)
									retSubstitions.add(sub);
							}
						}
					}
				}
			}
		}
		return retSubstitions;
	}

	private List<AtomicSentence> getConstantPremises(Sentence sentence) 
	{
		List<AtomicSentence> constantPremises = new ArrayList<AtomicSentence>();
		for(AtomicSentence premise : sentence.getPremises())
		{
			if(premise.isConstantSentence())
			{
				constantPremises.add(premise);
			}
		}
		return constantPremises;
	}

	private boolean inferAllPremises(Sentence sentence) 
	{
		boolean isAllpremisesTrue = true; 
		for(AtomicSentence premise : sentence.getPremises())
		{
			isAllpremisesTrue = inferBCRecursive(premise);
			if(!isAllpremisesTrue)
			{
				break;
			}
		}
		return isAllpremisesTrue;
	}


	/**
	 * @return the query
	 */
	public AtomicSentence getQuery() {
		return query;
	}

	/**
	 * @param query the query to set
	 */
	public void setQuery(String query) 
	{
		this.query = new AtomicSentence(query);
	}

	public void addSentenceToKB(String sentence) 
	{
		this.kb.addSentence(sentence);
	}
}
