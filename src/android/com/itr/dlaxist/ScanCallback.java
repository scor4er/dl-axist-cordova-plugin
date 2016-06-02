package com.itr.dlaxist;

/**
 * Simple callback interface to use for proxy results.
 * @param <ResultT>
 */
public interface ScanCallback<ResultT> {

    public void execute(ResultT result);
}