package com.vpp.core.suggestion;

import java.util.Date;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class SuggestionService implements ISuggestionService{
	
	@Autowired
	private  SuggestionMapper suggestionMapper;

	@Override
	public int insertService(Suggestion suggestion) {
		suggestion.setGmtCreate(new Date());
		return  suggestionMapper.insertSelective(suggestion);
	}
	

}
