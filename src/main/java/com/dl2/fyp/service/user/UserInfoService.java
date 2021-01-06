package com.dl2.fyp.service.user;

import com.dl2.fyp.entity.UserInfo;
import com.dl2.fyp.service.risk.RiskService;
import com.dl2.fyp.util.UpdateUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.Date;


/**
 * user info service
 */
@Service
public class UserInfoService {
    private static Logger LOG = LoggerFactory.getLogger(UserInfoService.class);

    @Autowired
    private RiskService riskService;
    /**
     * create user info
     * @param userInfo
     * @return
     */
    public UserInfo setUserInfo(UserInfo userInfo){
        LOG.debug("set user info, parameter:{}", userInfo);

        if (!checkInfo(userInfo)) return null;

        setDefault(userInfo);
        userInfo.setAcceptableRisk(riskService.calculateRiskFromUserInfo(userInfo));
        userInfo.setMonthlyExpense(
                userInfo.getHousingExpense()
                .add(userInfo.getLivingExpense())
                .add(userInfo.getMiscelExpense())
                .add(userInfo.getTaxExpense().divide(BigDecimal.valueOf(12), 2, RoundingMode.HALF_UP))
        );
        LOG.debug("set user info, result:{}",userInfo);
        return userInfo;
    }

    /**
     * update user info and return for update
     * @param oldInfo
     * @param newInfo
     * @return
     */
    public UserInfo updateUserInfo(UserInfo oldInfo, UserInfo newInfo){
        LOG.debug("create user info, parameter:{}", oldInfo);
        UpdateUtil.copyNullProperties(newInfo, oldInfo);
        LOG.debug("create user info, result:{}",oldInfo);
        return oldInfo;
    }

    /**
     * set the default value
     * @param userInfo
     */
    private void setDefault(UserInfo userInfo) {
        if (userInfo.getDateOfBirth()==null) userInfo.setDateOfBirth(new Date(0));
        if (userInfo.getMaritalStatus()==null) userInfo.setMaritalStatus(false);
        if (userInfo.getChildNum()==null) userInfo.setChildNum(Byte.valueOf("0"));
        if (userInfo.getCommission()==null) userInfo.setCommission(new BigDecimal(0));
        if (userInfo.getDebt()==null) userInfo.setCommission(new BigDecimal(0));
        if (userInfo.getDebtRate()==null) userInfo.setDebtRate(new BigDecimal(0));
        if (userInfo.getDividendCollectionFee()==null) userInfo.setDividendCollectionFee(new BigDecimal(0));
        if (userInfo.getEquity()==null) userInfo.setEquity(new BigDecimal(0));
        if (userInfo.getFamilyNum()==null) userInfo.setFamilyNum(Byte.valueOf("0"));
        if (userInfo.getMonthlyIncome()==null) userInfo.setMonthlyIncome(new BigDecimal(0));
        if (userInfo.getHousingExpense()==null) userInfo.setHousingExpense(new BigDecimal(0));
        if (userInfo.getLivingExpense()==null) userInfo.setLivingExpense(new BigDecimal(0));
        if (userInfo.getMiscelExpense()==null) userInfo.setMiscelExpense(new BigDecimal(0));
        if (userInfo.getTaxExpense()==null) userInfo.setTaxExpense(new BigDecimal(0));
    }

    /**
     * check the info data
     * @param userInfo
     */
    private boolean checkInfo(UserInfo userInfo) {
        Date date = userInfo.getDateOfBirth();
        if(date != null && date.before(new Date()) && date.after(new Date(0))) return true;
        return false;
    }


}
