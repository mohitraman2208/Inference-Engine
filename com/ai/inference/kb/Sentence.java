package com.ai.inference.kb;

import java.util.ArrayList;
import java.util.List;

public class Sentence {
	
	private AtomicSentence conclusion;
	
	private List<AtomicSentence> premises;
	
	
	private boolean isAtomicSentence;
	
	public Sentence()
	{
		premises = new ArrayList<AtomicSentence>();
		setAtomicSentence(false);
	}
	
	/*public Sentence(String conclusion,String[] premises)
	{
		this();
		createSentence(conclusion,premises);
	}
	*/
	
	public Sentence(AtomicSentence conclusion, List<AtomicSentence> premises)
	{
		this.conclusion = conclusion;
		this.premises = premises;
	}
	
	public Sentence(String sentenceStr) 
	{
		this();
		createSentence(sentenceStr);
	}

	
	/**
	 * @return the premises
	 */
	public List<AtomicSentence> getPremises() {
		return premises;
	}

	/**
	 * @param premises the premises to set
	 */
	public void setPremises(List<AtomicSentence> premises) {
		this.premises = premises;
	}

	/**
	 * @return the conclusion
	 */
	public AtomicSentence getConclusion() {
		return conclusion;
	}

	/**
	 * @param conclusion the conclusion to set
	 */
	public void setConclusion(AtomicSentence conclusion) {
		this.conclusion = conclusion;
	}
	
	/**
	 * @return the isAtomicSentence
	 */
	public boolean isAtomicSentence() {
		return isAtomicSentence;
	}

	/**
	 * @param isAtomicSentence the isAtomicSentence to set
	 */
	public void setAtomicSentence(boolean isAtomicSentence) {
		this.isAtomicSentence = isAtomicSentence;
	}

	public void createSentence(String conclusion,String[] premisesStrArr)
	{
		this.conclusion = new AtomicSentence(conclusion);
		
		for(String premisesStr : premisesStrArr)
		{
			AtomicSentence premise = new AtomicSentence(premisesStr.trim());
			this.premises.add(premise);
		}
	}
	
	private void createSentence(String sentenceStr) 
	{
		if(sentenceStr.lastIndexOf("=>") > 0)
		{
			String[] sentenceTokens = sentenceStr.split("=>");
			createSentence(sentenceTokens[1].trim(),sentenceTokens[0].split("&"));
		}
		else
		{
			setAtomicSentence(true);
			this.conclusion = new AtomicSentence(sentenceStr.trim());
		}
	}

	public void getConstantNames(List<String> arguments, List<String> resultList)
	{
		for(String constantName : arguments)
		{
			if(Character.isUpperCase(constantName.trim().charAt(0)))
				resultList.add(constantName);
		}
	}

	public void getConstantNames( List<String> resultList) 
	{
		for(AtomicSentence pr : this.premises)
		{
			getConstantNames(pr.getArguments(), resultList);
		}
		getConstantNames(conclusion.getArguments(), resultList);
	}

	public void copy(Sentence sentence)
	{
		this.conclusion = new AtomicSentence(sentence.getConclusion());
		for(AtomicSentence atom : sentence.getPremises())
		{
			this.premises.add(new AtomicSentence(atom));
		}
		this.isAtomicSentence = sentence.isAtomicSentence;
	}

	@Override
	public String toString() {
		StringBuffer premiseBuff = new StringBuffer();
		for(AtomicSentence premise : premises)
		{
			premiseBuff.append(premise.toString()).append("&");
		}
		return new StringBuffer().append(premiseBuff).append("=>").append(conclusion.toString()).toString();
	}

	public static boolean hasAllConstantPremises(Sentence sentence) {
		boolean retval = true;
		for(AtomicSentence premise : sentence.getPremises())
		{
			if(!premise.isConstantSentence())
			{
				retval = false;
				break;
			}
		}
		return retval;
	}
}
