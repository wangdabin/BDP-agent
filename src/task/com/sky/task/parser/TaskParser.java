package com.sky.task.parser;

import com.sky.parser.Parser;
import com.sky.parser.ParserFactory;


/**
 * 任务解析
 * @author Dragon Joey
 *
 */
public interface TaskParser extends Parser{
	
	void setParserFactory(ParserFactory factory);
}
