package com.vpp.core.coinguess.service.impl;

import java.io.IOException;
import java.math.BigDecimal;
import java.text.ParseException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.ibatis.annotations.Param;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import com.github.pagehelper.Page;
import com.github.pagehelper.PageHelper;
import com.vpp.common.utils.DateUtil;
import com.vpp.common.utils.HttpSenderUtils;
import com.vpp.core.coinguess.bean.Coinguess;
import com.vpp.core.coinguess.bean.PriceInfo;
import com.vpp.core.coinguess.mapper.CoinguessMapper;
import com.vpp.core.coinguess.service.ICoinguessService;
import com.vpp.core.customer.bean.Customer;
import com.vpp.core.customer.service.ICustomerService;
import com.vpp.core.standardized.productcoinguess.bean.ProductCoinguess;
import com.vpp.core.standardized.productcoinguess.service.IProductCoinguessService;

import net.sf.json.JSONObject;

@Service
public class CoinguessService implements ICoinguessService {
    private static final Logger logger = LogManager.getLogger(CoinguessService.class); // 打log

    @Autowired
    private CoinguessMapper coinguessMapper;

    @Autowired
    private ICustomerService customerService;

    @Autowired
    private IProductCoinguessService productCoinguessService;

    // 请求币价url
    public static String QUERY_COIN_PRICE_HISTORY = "http://119.28.202.93:8004/bian/detail/";
    public static String QUERY_COIN_PRICE_REALTIME = "http://119.28.202.93:8005/bian/redisdetail/";
//    public static String QUERY_ORDER_PRICE = "http://119.28.202.93:8006/bian/redis/order/detail/";
//    public static String QUERY_ACTUAL_PRICE = "http://119.28.202.93:8007/bian/redis/result/detail/";

    //移动平均价格URL
    public static String QUERY_AVERAGE_ORDER_PRICE = "http://119.28.202.93:8011/bian/redis/order/detail/";
    public static String QUERY_AVERAGE_ACTUAL_PRICE = "http://119.28.202.93:8012/bian/redis/result/detail/";
    // @Autowired
    // private Coinguess coinguess;

    /**
     * 新增猜币价订单
     *
     * @param coinguess
     * @return 1-插入成功; 0-插入失败
     */
    @Override
    @Transactional(propagation = Propagation.REQUIRED, rollbackFor = Exception.class)
    public int insertCoinguessOrder(Coinguess coinguess, Long customerId, Long ownerId, BigDecimal orderAmt) {
        int res = 1;
        try {
            // 客户支出金额
            customerService.expenditureBalance(customerId, orderAmt, "猜币下单-用户，订单ID： " + coinguess.getOrderId());
            // 庄家收入金额
            customerService.incomeBalance(ownerId, orderAmt, "猜币下单-庄家，订单ID：" + coinguess.getOrderId());

            coinguessMapper.insertSelective(coinguess);
        } catch (Exception e) {
            res = 0;
            logger.error("插入order_list_coin表出错:" + e.getMessage());
            throw new RuntimeException("插入order_list_coin表出错");
        }
        return res;
    }

    @Transactional(propagation = Propagation.REQUIRED, rollbackFor = Exception.class)
    public int refundCoinguessOrder(Coinguess coinguess, Long customerId, Long contractOwnerId, BigDecimal orderAmt) {
        int res = 1;
        try {
            // 客户收入金额
            customerService.incomeBalance(customerId, orderAmt, "猜币退款-用户，订单ID： " + coinguess.getOrderId());
            // 庄家支出金额
            customerService.expenditureBalance(contractOwnerId, orderAmt, "猜币退款-庄家，订单ID：" + coinguess.getOrderId());

            updateCoinguessOrder(coinguess);
        } catch (Exception e) {
            res = 0;
            logger.error("订单退款出错:" + coinguess.getOrderId() + "错误信息： " +  e.getMessage());
            throw new RuntimeException("订单退款出错");
        }
        return res;
    }

    /**
     * 根据主键查询币价订单
     *
     * @param id
     * @return Coinguess对象
     */
    @Override
    public Coinguess selectCoinguessOrderById(Long id) {
        return coinguessMapper.selectByPrimaryKey(id);
    }

    /**
     * 根据订单号（系统生成）查询币价订单
     *
     * @param orderId
     * @return Coinguess对象
     */
    @Override
    public Coinguess selectCoinguessOrderByOrderId(String orderId) {
        return coinguessMapper.selectByOrderId(orderId);
    }

    /**
     * 根据用户id和投注时间戳查找记录是否存在
     *
     * @param customerId
     * @param orderTs
     * @return true - 存在，false - 不存在
     */
    @Override
    public int checkOrderExistence(@Param("customerId") Long customerId, @Param("orderTs") String orderTs) {
        return coinguessMapper.selectByCustomerIdAndOrderTs(customerId, orderTs);
    }

    /**
     * 更新猜币价订单
     *
     * @param coinguess
     * @return 1-更新成功; 0-更新失败
     */
    @Override
    public int updateCoinguessOrder(Coinguess coinguess) {
        int res = 1;
        try {
            coinguessMapper.updateByPrimaryKey(coinguess);
        } catch (Exception e) {
            res = 0;
            logger.error("发生异常:" + e.getMessage());
            throw new RuntimeException("更新order_list_coin表出错");
        }
        return res;
    }

    /**
     * 根据客户id查猜币订单列表
     *
     * @param pageNum
     * @param pageSize
     * @param customerId
     * @return
     */
    @Override
    public Page<Coinguess> selectCoinguessInfoByCustomerId(int pageNum, int pageSize, @Param("customerId") Long customerId) {
        PageHelper.startPage(pageNum, pageSize);

        return coinguessMapper.selectCoinguessInfoByCustomerId(customerId);
    }

    /**
     * 根据开奖时间查询待开奖的数据list
     *
     * @return list<Coinguess>
     */
    public List<Coinguess> selectCoinguessInfoByStatus(String lotteryTime) {
        return coinguessMapper.selectCoinguessInfoByStatus(lotteryTime);
    }

    /**
     * 获得同一开奖时间的订单总金额
     *
     * @param lotteryTime
     * @return 订单总金额
     */
    @Override
    public BigDecimal getTotalOrderAmountByLotteryTime(String lotteryTime) {
        return coinguessMapper.getTotalOrderAmountByLotteryTime(lotteryTime);
    }

    /**
     * 获得多个订单总收益
     *
     * @param idList
     * @return 订单总金额
     */
    @Override
    public BigDecimal getTotalProfit(List<Long> idList) {
        return coinguessMapper.getTotalProfit(idList);
    }

    /**
     * 获得同一客户在同一期的下单数量
     *
     * @param lotteryTime
     * @return 下单数量
     */
    @Override
    public Integer getTotalOrders(Long customerId, String lotteryTime) {
        return coinguessMapper.getTotalOrders(customerId, lotteryTime);
    }

    /**
     * 批量获得下单价格
     * @param lotteryTime lotterytime必须要是是整分钟
     * @return
     */
    public void batchUpdateOrderPrice(Long lotteryTime) {

        // 获取产品信息
        List<ProductCoinguess> coinguessProductList = productCoinguessService.selectProductCoinguessByStatusFull();

        // 产品id - 产品信息map缓存
        Map<String,ProductCoinguess> coinguessProductMap = new HashMap<String,ProductCoinguess>();
        for(ProductCoinguess item:coinguessProductList){
            coinguessProductMap.put(item.getProductId(),item);
        }


        List<Coinguess> coinguess = selectCoinguessInfoByStatus(DateUtil.unixTimestampToTimestamp(lotteryTime.toString()));


        // 检查是否有待开奖的数据，如无待开奖数据则直接返回
        if (coinguess.size() == 0) {
            return;
        }

        // （产品id_开奖时间，下单价格）缓存map
        Map<String,BigDecimal> productLotteryTimeOrderPriceMap = new HashMap<String,BigDecimal>();

        // 对未开奖记录获取下单价格，如果有缓存则取缓存价格
        for (Coinguess item : coinguess) {

//            //获取移动平均价格 下两行注释
//            Long realOrderTime = lotteryTime - 30000L;  // 下单时间被固定在30秒处，30000L = 30秒
//            String key = item.getTargetId() + realOrderTime.toString();
            //获取移动平均价格
            String key = item.getTargetId() + item.getOrderTsUnix();
            try {
                if (productLotteryTimeOrderPriceMap.get(key) == null) {  //如果map中未存，则请求下单价格
                    // String calOrderTime = (Long.parseLong(DateUtil.formatDateInMillisToEpochTime(item.getLotteryTime())) - 30000L) + "";
                    BigDecimal orderPrice = getOrderPriceForBatch(item.getTargetId(), item.getOrderTsUnix().toString()); // 获取下单价


                    if(orderPrice != null){
                        productLotteryTimeOrderPriceMap.put(key, orderPrice);  //缓存在map中
                        item.setOrderPrice(orderPrice); // 更新订单表下单价格
                        updateCoinguessOrder(item); // 更新数据库
                    }
                } else {
                    item.setOrderPrice(productLotteryTimeOrderPriceMap.get(key)); // 直接取map中的价格更新订单表下单价格
                    updateCoinguessOrder(item); // 更新数据库

                }
            } catch (Exception e) {
                logger.error("请求下单价格出错：" + "产品ID： " + item.getTargetId() + "下单时间： " + item.getOrderTsUnix() + "错误信息" + e.getMessage());
                continue;
            }

        }

    }

    /**
     * 批量退款（5分钟内无法开奖则退款）
     *
     * @return
     */
    public void batchRefund() {
        Long currentLotteyTime = ((System.currentTimeMillis() / 60000L) + 1L) * 60000L; // 推算当前开奖时间，60000 ms = 1分钟
        Long fiveMinsBefore = currentLotteyTime - 5L * 60000L;

        List<Coinguess> coinguessList = selectForRefund(DateUtil.unixTimestampToTimestamp(fiveMinsBefore.toString()));

        // 检查是否有待退款的数据，如无数据则直接返回
        if (coinguessList.size() == 0) {
            return;
        }

        // 获取产品信息
        List<ProductCoinguess> coinguessProductList = productCoinguessService.selectProductCoinguessByStatusFull();

        // 产品id - 产品信息map缓存
        Map<String, ProductCoinguess> coinguessProductMap = new HashMap<String, ProductCoinguess>();
        for (ProductCoinguess item : coinguessProductList) {
            coinguessProductMap.put(item.getProductId(), item);
        }

        Map<String, Customer> productContractOwnerMap = new HashMap<String, Customer>();

        for (Coinguess coinguess : coinguessList) {
            Long customerId = coinguess.getCustomerId();
            Long contractOwnerId = coinguessProductMap.get(coinguess.getTargetId()).getCustomerId();

            coinguess.setStatus(3); //订单状态 3-退款
            coinguess.setRewardFlg(2); //赔付状态 2-退款

            try {
                refundCoinguessOrder(coinguess,customerId,contractOwnerId,coinguess.getOrderAmt());
            } catch (Exception e) {
                continue;
            }

        }
    }



    private List<Coinguess> selectForRefund(String lotteryTime) {
        return coinguessMapper.selectForRefund(lotteryTime);
    }

    /**
     * 前端未发开奖请求时，系统批量开奖，增强系统鲁棒性
     * 只开最近三期，开不了奖的5分钟内会自动退款： batchRefund()
     *
     * @param currentTimestamp
     */
    @Override
    public void batchLottery(Long currentTimestamp){
        Long currentLotteryTime = ((currentTimestamp / 60000L) * 60000L);  //把秒位去掉，代表当前一分钟
        Long lastLotteryTime = currentLotteryTime - 60000L;  //上一分钟
        Long lastTwoLotteryTime = currentLotteryTime - 120000L;  //上两分钟

        // 最新一期开奖
        batchUpdateOrderPrice(currentLotteryTime);
        List<Coinguess> currentCoinguessList = selectCoinguessInfoByStatus(DateUtil.unixTimestampToTimestamp(currentLotteryTime.toString()));
        for(Coinguess item : currentCoinguessList){
            runTheLottery(item.getCustomerId(), currentLotteryTime.toString());
        }

        // 上一期开奖
        batchUpdateOrderPrice(lastLotteryTime);
        List<Coinguess> lastCoinguessList = selectCoinguessInfoByStatus(DateUtil.unixTimestampToTimestamp(lastLotteryTime.toString()));
        for(Coinguess item : lastCoinguessList){
            runTheLottery(item.getCustomerId(), lastLotteryTime.toString());
        }

        // 上两期开奖
        batchUpdateOrderPrice(lastTwoLotteryTime);
        List<Coinguess> lastTwoCoinguessList = selectCoinguessInfoByStatus(DateUtil.unixTimestampToTimestamp(lastTwoLotteryTime.toString()));
        for(Coinguess item : lastTwoCoinguessList){
            runTheLottery(item.getCustomerId(), lastTwoLotteryTime.toString());
        }
    }


    /**
     * 给单个客户开奖，将该客户在特定开奖时间，未开奖的数据筛选出来开奖
     *
     * @param custId 客户号
     * @param lotteryTime 开奖时间
     * @return  BigDecimal
     */
    @Override
    @Transactional(propagation = Propagation.REQUIRED, rollbackFor = Exception.class)
    public BigDecimal runTheLottery(Long custId, String lotteryTime) {

        System.out.println("开始开奖" + "客户号：" + custId + "开奖时间" + lotteryTime +  " " + DateUtil.unixTimestampToTimestamp(lotteryTime));

        BigDecimal totalWin = BigDecimal.ZERO;
        List<Coinguess> list = selectCoinguessInfoByCustomerIdAndStatus(custId, DateUtil.unixTimestampToTimestamp(lotteryTime)); //获取客户在特定开奖时间未开奖的记录

        System.out.println("开奖条数: " + list.size());

        // 如无待开奖数据则直接返回
        if (list.size() == 0) {
            return BigDecimal.ZERO;
        }

        // 产品id - 产品信息缓存map
        Map<String, ProductCoinguess> productCoinguessMap = new HashMap<String, ProductCoinguess>();

        // 产品id - 开奖价格信息缓存map
        Map<String, ActualPriceInfo> actualPriceInfoMap = new HashMap<String, ActualPriceInfo>();

        for (Coinguess item : list) {
            String targetId = item.getTargetId();
            if (productCoinguessMap.get(targetId) == null) {  //在map中不存在的才查产品表并缓存，已存在的不再查
                ProductCoinguess productCoinguess = productCoinguessService.selectByProductId(item.getTargetId());
                productCoinguessMap.put(targetId, productCoinguess);

                try {
                    actualPriceInfoMap.put(targetId, getActualPriceForBatch(targetId, lotteryTime)); //请求一次
                    System.out.println("请求开奖币价结果" + actualPriceInfoMap.get(targetId).getActualPrice() );
                } catch (IOException e) {
                    logger.error("请求开奖价格出错（一次）：" + "产品ID： " + targetId + "开奖时间： " + lotteryTime + "错误信息" + e.getMessage()); // 第一次出错
                    //todo: 是否有必要请求第二次
                    try {
                        actualPriceInfoMap.put(targetId, getActualPriceForBatch(targetId, lotteryTime)); //再请求一次
                    } catch (IOException e1) {
                        logger.error("请求开奖价格出错（二次）：" + "产品ID： " + targetId + "开奖时间： " + lotteryTime + "错误信息" + e.getMessage()); // 第二次出错
                    }
                }
            }
        }

        // 批处理：未开奖的数据，对比下单时价格和开奖时点价格，分别开奖，更新客户和庄家余额及订单表
        for (Coinguess coinguess : list) {

            String targetId = coinguess.getTargetId();
            BigDecimal actualPrice = actualPriceInfoMap.get(targetId).getActualPrice();   //取缓存开奖价格

            BigDecimal orderPrice = coinguess.getOrderPrice();
            String actualLotteryTimeInUnix = actualPriceInfoMap.get(targetId).getActual_lottery_time(); //取缓存实际开奖时间 in unix
            if(actualLotteryTimeInUnix == null) continue;
            String actualLotteryTimeInDbTimestamp = DateUtil.unixTimestampToTimestamp(actualLotteryTimeInUnix); //时间转换为db的格式

            // 开奖信息赋值
            coinguess.setActualPrice(actualPrice);
            coinguess.setActualLotteryTimeUnix(actualLotteryTimeInUnix);
            coinguess.setActualLotteryTime(actualLotteryTimeInDbTimestamp);

            if (orderPrice == null || actualPrice == null || coinguess.getOrderDir() == null) {
                logger.error("" + " orderPrice == null || actualPrice == null || coinguess.getOrderDir() == null " + "，无法开奖");
                continue;
            }

            // （下单价格小于开奖价格 且 0-猜涨）或 （下单价格大于开奖价格 且 1-猜跌） 则用户赢
            if ((coinguess.getOrderPrice().compareTo(actualPrice) < 0 && coinguess.getOrderDir() == 0)
                    || (coinguess.getOrderPrice().compareTo(actualPrice) > 0 && coinguess.getOrderDir() == 1)) {
                coinguess.setStatus(2); // 2-赢

                Long customerId = coinguess.getCustomerId(); // 用户id
                Long contractOwnerId = productCoinguessMap.get(targetId).getCustomerId(); // 合约所有者id

                totalWin = totalWin.add(coinguess.getProfit());

                // 更新用户账户余额，给账户余额加上本次盈利
                customerService.incomeBalance(customerId, coinguess.getProfit(), "猜币开奖-用户赢，订单ID：" + coinguess.getOrderId());

                Customer contractOwner = customerService.selectCustomerById(contractOwnerId);

                //  庄家相应减少余额（如有庄家）
                if (contractOwnerId != 0 && contractOwnerId != null) {
                    // todo: 风控待办，根据产品参数表控制，合约所有者需保持 保证金余额 >= profit_rate * single_term_limit * risk_control_term_limit,
                    // 否则告警，低于此金额1/2平仓

                    Float profitRate = productCoinguessMap.get(targetId).getProfitRate();
                    BigDecimal singleTermLimit = productCoinguessMap.get(targetId).getSingleTermLimit();
                    int riskControlTermLimit = productCoinguessMap.get(targetId).getRiskControlTermLimit();
                    BigDecimal contractOwnerBalanceWarning = new BigDecimal(profitRate).multiply(singleTermLimit)
                            .multiply(new BigDecimal(riskControlTermLimit));
                    BigDecimal contractOwnerBalanceBottomLine = contractOwnerBalanceWarning.multiply(new BigDecimal(0.5));

                    // System.out.println("contractOwnerBalanceWarning:_______________" + contractOwnerBalanceWarning);
                    // System.out.println("contractOwnerBalanceBottomLine:_______________" + contractOwnerBalanceBottomLine);
                    // System.out.println("contractOwner.getBalance():_______________" + contractOwner.getBalance());

                    /*
                     * if(contractOwner.getBalance().compareTo(contractOwnerBalanceBottomLine) < 0){ // todo: 平仓 }else
                     * if(contractOwner.getBalance().compareTo(contractOwnerBalanceWarning) < 0){ // todo: 告警 }
                     */

                    // 检查合约所有者是否能够赔付
                    if (contractOwner.getBalance().compareTo(coinguess.getProfit()) > 0) {
                        try {
                            customerService.expenditureBalance(contractOwnerId, coinguess.getProfit(),
                                    "猜币开奖-庄家输，订单ID： " + coinguess.getOrderId());
                        } catch (Exception e) {
                            logger.error("更新合约所有者：" + contractOwnerId + " 的余额失败，跳过处理");
                            continue;
                        }
                    } else {
                        logger.error("合约所有者：" + contractOwnerId + " 保证金不足，暂无法赔付，跳过处理");
                        continue;
                    }
                }
            } else {
                coinguess.setStatus(1); // 1-输
            }

            coinguess.setRewardFlg(1); // 更新赔付状态为：1-已赔付
            // coinguess.setGmtModified(DateUtil.getCurrentDateTimeLocal()); 数据库自动更新
            updateCoinguessOrder(coinguess);
        }

        return totalWin;
    }


    /**
     * 通过客户号和,开奖时间和开奖状态筛选该客户未开奖的数据
     *
     * @param customerId
     * @return List<Coinguess>
     */
    @Override
    public List<Coinguess> selectCoinguessInfoByCustomerIdAndStatus(Long customerId, String lotteryTime) {
        return coinguessMapper.selectCoinguessInfoByCustomerIdAndStatus(customerId, lotteryTime);
    }

    /**
     * 通过post查询币价实时信息，key值为1-货币对名；2-开奖时间；3-下订单时间
     *
     * @param coinguess
     * @return PriceInfo
     */
    public PriceInfo getCoinPirceInRealtimeByPost(Coinguess coinguess) throws IOException {
        // System.out.println("_____________realtime price");

        PriceInfo priceInfo = new PriceInfo();
        // 准备post key
        Map<String, Object> params = new HashMap<String, Object>();
        String resultTime = null;
        String orderTime = null;

        try {
            resultTime = DateUtil.formatDateInMillisToEpochTime(coinguess.getLotteryTime());
            orderTime = coinguess.getOrderTsUnix();

        } catch (ParseException e) {
            logger.error("下单时间或开奖时间戳非法，无法开奖，订单号：" + coinguess.getId());
        }

        params.put("coin", coinguess.getTargetId());
        params.put("result_time", resultTime);
        params.put("order_time", orderTime);


        String json = null;

        json = HttpSenderUtils.sendPostMap(QUERY_COIN_PRICE_REALTIME, params); // 异常在上一层handle

        // json解析
        JSONObject jsonObj = JSONObject.fromObject(json);
        String returnCode = null;
        BigDecimal orderPrice = null;
        BigDecimal actualPrice = null;
        String actualLotteryTime = null;

        returnCode = jsonObj.get("code").toString();

        if ("0".equals(returnCode)) {
            // 下单时点币价
            orderPrice = new BigDecimal((JSONObject.fromObject(jsonObj.get("result")).get("o_deal_price").toString()));
            // 开奖时点币价
            actualPrice = new BigDecimal(JSONObject.fromObject(jsonObj.get("result")).get("r_deal_price").toString());
            // 实际开奖时间
            actualLotteryTime = JSONObject.fromObject(jsonObj.get("result")).get("r_deal_time").toString();

            priceInfo.setOrderPrice(orderPrice);
            priceInfo.setActualPrice(actualPrice);
            priceInfo.setActualLotteryTime(actualLotteryTime);

            if (orderPrice != null && actualPrice != null && actualLotteryTime != null) {
                priceInfo.setSuccessGetPrice(true);
            }

            return priceInfo;

        } else {
            priceInfo.setSuccessGetPrice(false);

            return priceInfo;
        }


    }

    /**
     * 通过post查询开奖币价，key值为1-货币对名；2-开奖时间；
     *
     * @param targetId    货币对名
     * @param lotteryTime 开奖时间
     * @return BigDecimal actualPrice 开奖价格
     */
    public ActualPriceInfo getActualPriceForBatch(String targetId, String lotteryTime) throws IOException {


        ActualPriceInfo actualPriceInfo = new ActualPriceInfo();
        Map<String, Object> params = new HashMap<String, Object>();
        params.put("coin", targetId);
        params.put("result_time", lotteryTime);

        String json = null;
        json = HttpSenderUtils.sendPostMap(QUERY_AVERAGE_ACTUAL_PRICE, params); // 异常在上一层handle

        // json解析
        JSONObject jsonObj = JSONObject.fromObject(json);
        String returnCode = null;
        BigDecimal actualPrice = null;

        returnCode = jsonObj.get("code").toString();

        if ("0".equals(returnCode)) { // returnCode等于0时取价格
            actualPriceInfo.setActualPrice(new BigDecimal(JSONObject.fromObject(jsonObj.get("result")).get("r_deal_price").toString()));
            actualPriceInfo.setActual_lottery_time(JSONObject.fromObject(jsonObj.get("result")).get("r_deal_time").toString());  //unix毫秒时间戳，并未转换的
        }

        return actualPriceInfo;

    }

    /**
     * 通过post查询下单币价，key值为1-货币对名；2-下单时间；
     *
     * @param targetId    货币对名
     * @param orderTsUnix 下单时间
     * @return BigDecimal orderPrice 下单价格
     */
    public BigDecimal getOrderPriceForBatch(String targetId, String orderTsUnix) throws IOException {


        Map<String, Object> params = new HashMap<String, Object>();
        params.put("coin", targetId);
        params.put("order_time", orderTsUnix);

        String json = null;
        //json = HttpSenderUtils.sendPostMap(QUERY_ORDER_PRICE, params); // 异常在上一层handle
        //获取均线价格
        json = HttpSenderUtils.sendPostMap(QUERY_AVERAGE_ORDER_PRICE, params); // 异常在上一层handle

        // json解析
        JSONObject jsonObj = JSONObject.fromObject(json);
        String returnCode = null;
        BigDecimal actualPrice = null;
        BigDecimal orderPrice = null;

        returnCode = jsonObj.get("code").toString();

        if ("0".equals(returnCode)) { // returnCode等于0时取价格
            // 下单时点币价
            orderPrice = new BigDecimal((JSONObject.fromObject(jsonObj.get("result")).get("o_deal_price").toString()));
        }


        if(orderPrice == null){
            logger.error("产品id： " + targetId + " 下单时间戳： " + orderTsUnix + " 未请求到下单价");
        }
        return orderPrice;

    }

    @Override
    public List<Map<String, Object>> findCount(String startGmtCreate, String endGmtCreate) {
        return coinguessMapper.findCount(startGmtCreate, endGmtCreate);
    }

    @Override
    public List<Map<String, Object>> findPayout(String startGmtCreate, String endGmtCreate) {
        return coinguessMapper.findPayout(startGmtCreate, endGmtCreate);
    }
}



class ActualPriceInfo {
    private BigDecimal actualPrice;

    public BigDecimal getActualPrice() {
        return actualPrice;
    }

    public void setActualPrice(BigDecimal actualPrice) {
        this.actualPrice = actualPrice;
    }

    public String getActual_lottery_time() {
        return actual_lottery_time;
    }

    public void setActual_lottery_time(String actual_lottery_time) {
        this.actual_lottery_time = actual_lottery_time;
    }

    private String actual_lottery_time;

}


