package com.vpp.core.suggestion.service.impl;

import java.util.Date;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.vpp.core.suggestion.bean.Suggestion;
import com.vpp.core.suggestion.mapper.SuggestionMapper;
import com.vpp.core.suggestion.service.ISuggestionService;

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
