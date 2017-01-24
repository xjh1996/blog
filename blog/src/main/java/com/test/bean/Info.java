package com.test.bean;

public class Info {
    private Integer id;

    private Integer todayClickTimes;

    private Integer historyClickTimes;

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public Integer getTodayClickTimes() {
        return todayClickTimes;
    }

    public void setTodayClickTimes(Integer todayClickTimes) {
        this.todayClickTimes = todayClickTimes;
    }

    public Integer getHistoryClickTimes() {
        return historyClickTimes;
    }

    public void setHistoryClickTimes(Integer historyClickTimes) {
        this.historyClickTimes = historyClickTimes;
    }
}