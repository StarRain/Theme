package com.ldfs.theme.callback;

/**
 * 执行任务体,带参数
 * 
 * @author momo
 * @Date 2014/6/24
 */
public interface ITask<T> {
	void run(T... ts);
}
