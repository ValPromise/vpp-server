package com.vpp.vo;

import java.util.List;

/**
 * transaction 查询bean
 * 
 * @author DELL
 *
 */
public class ApiTx {
	// 区块号
	private String blockNumber;
	// 时间戳
	private String timeStamp;
	// transaction hash编号
	private String hash;
	// 交易号
	private String nonce;
	// 区块hash
	private String blockHash;
	//
	private String transactionIndex;
	// from
	private String from;
	// to
	private String to;
	// ETH
	private String value;
	// -----交易费 = gasUsed * gasPrice(Gwei)
	// Gas Limit
	private String gas;
	// Gas Price
	private String gasPrice;
	// 0:success,1:error
	private String isError;
	// TxReceipt Status 0:error,1:Success
	private String txreceipt_status;
	// 入参
	private String input;
	// 合约地址
	private String contractAddress;
	// 追加手续费
	private String cumulativeGasUsed;
	// 交易手续费
	private String gasUsed;
	// 已确认区块数
	private String confirmations;

	// input 解析后methodID，参数集合 ------------start
	private String methodID;
	private List<String> params;
	// input 解析后methodID，参数集合 ------------end

	public String getBlockNumber() {
		return blockNumber;
	}

	public void setBlockNumber(String blockNumber) {
		this.blockNumber = blockNumber;
	}

	public String getTimeStamp() {
		return timeStamp;
	}

	public void setTimeStamp(String timeStamp) {
		this.timeStamp = timeStamp;
	}

	public String getHash() {
		return hash;
	}

	public void setHash(String hash) {
		this.hash = hash;
	}

	public String getNonce() {
		return nonce;
	}

	public void setNonce(String nonce) {
		this.nonce = nonce;
	}

	public String getBlockHash() {
		return blockHash;
	}

	public void setBlockHash(String blockHash) {
		this.blockHash = blockHash;
	}

	public String getTransactionIndex() {
		return transactionIndex;
	}

	public void setTransactionIndex(String transactionIndex) {
		this.transactionIndex = transactionIndex;
	}

	public String getFrom() {
		return from;
	}

	public void setFrom(String from) {
		this.from = from;
	}

	public String getTo() {
		return to;
	}

	public void setTo(String to) {
		this.to = to;
	}

	public String getValue() {
		return value;
	}

	public void setValue(String value) {
		this.value = value;
	}

	public String getGas() {
		return gas;
	}

	public void setGas(String gas) {
		this.gas = gas;
	}

	public String getGasPrice() {
		return gasPrice;
	}

	public void setGasPrice(String gasPrice) {
		this.gasPrice = gasPrice;
	}

	public String getIsError() {
		return isError;
	}

	public void setIsError(String isError) {
		this.isError = isError;
	}

	public String getTxreceipt_status() {
		return txreceipt_status;
	}

	public void setTxreceipt_status(String txreceipt_status) {
		this.txreceipt_status = txreceipt_status;
	}

	public String getInput() {
		return input;
	}

	public void setInput(String input) {
		this.input = input;
	}

	public String getContractAddress() {
		return contractAddress;
	}

	public void setContractAddress(String contractAddress) {
		this.contractAddress = contractAddress;
	}

	public String getCumulativeGasUsed() {
		return cumulativeGasUsed;
	}

	public void setCumulativeGasUsed(String cumulativeGasUsed) {
		this.cumulativeGasUsed = cumulativeGasUsed;
	}

	public String getGasUsed() {
		return gasUsed;
	}

	public void setGasUsed(String gasUsed) {
		this.gasUsed = gasUsed;
	}

	public String getConfirmations() {
		return confirmations;
	}

	public void setConfirmations(String confirmations) {
		this.confirmations = confirmations;
	}

	public String getMethodID() {
		return methodID;
	}

	public void setMethodID(String methodID) {
		this.methodID = methodID;
	}

	public List<String> getParams() {
		return params;
	}

	public void setParams(List<String> params) {
		this.params = params;
	}

}
