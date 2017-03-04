/**
 * Created by john on 17/3/3.
 */
function putOneBlog(parentDiv, blog, comments) {
    parentDiv.empty();
    /////////////////////////////////////////////////
    var title = $('<h1 >' + blog.title + '</h1>');
    parentDiv.append(title);
    /////////////////////头部区///////////////////////////////
    var properties = $(' <div style="text-align: right;font-size: 16px;padding-top: 10px"></div>');
    var divtype = $('<div style="float: left">标签：</div>');
    var type = $('<a href="javascript:void(0);" class="label label-info" style="font-size: 16px">' + blog.type.typename + '</a>');
    type.attr('onclick', 'getBlogsByType(' + blog.type.id + ')');
    divtype.append(type);
    properties.append(divtype);
    var labelClickTimes = $('<label style="font-size: 15px;text-align: left"></label>');
    var iconClickTimes = $('<span class="glyphicon glyphicon-eye-open" style="color: gray" aria-hidden="true"></span>').text("阅读（" + blog.clickTimes + "）");
    //var hrefClickTimes=$('<a style="text-decoration:none;" href="#commentform"></a>').text("阅读（"+blog.clickTimes+"）");
    //iconClickTimes.append(hrefClickTimes);
    labelClickTimes.append(iconClickTimes);
    properties.append(labelClickTimes);
    var labelCommentTimes = $('<label style="font-size: 15px;text-align: left"></label>');
    var iconCommentTimes = $('<span class="glyphicon glyphicon-edit" style="color: gray" aria-hidden="true"></span>');
    var hrefCommentTimes = $('<a style="text-decoration:none;" href="javascript:void(0);" onclick="gotoComments()"></a>').text("评论");
    iconCommentTimes.append(hrefCommentTimes);
    iconCommentTimes.append("（" + blog.commentTimes + "）");
    labelCommentTimes.append(iconCommentTimes);
    properties.append(labelCommentTimes);
    var labelAgreeTime = $('<label style="font-size: 15px;text-align: left"></label>');
    var buttonAgree = $('<button class="btn"></button>');
    buttonAgree.attr('onclick', "addagree(" + blog.id + ")");
    var iconAgreeTimes = $('<span class="glyphicon glyphicon-thumbs-up button" style="color: gray" aria-hidden="true"></span>');
    var labelAgreeTimes = $('<label style="text-align: left"></label>').text("推荐(" + blog.agreeWithTimes + ")　");
    labelAgreeTimes.attr('id', "agree" + blog.id);
    buttonAgree.append(iconAgreeTimes);
    labelAgreeTime.append(buttonAgree);
    labelAgreeTime.append(labelAgreeTimes);
    properties.append(labelAgreeTime);
    var labelDate = $('<label style="font-size: 15px;"></label>').text(getSmpFormatDateByLong(blog.createTime, true));
    properties.append(labelDate);
    parentDiv.append(properties);
    ////////////////////////正文区/////////////////////////////
    parentDiv.append('<hr>');
    parentDiv.append(blog.content);
    ////////////////////////评论区//////////////////////////
    parentDiv.append('<h1 class="page-header">评论区</h1>');
    var divComments = $('<div id="commentArea"></div>');
    putComments(divComments, comments);
    parentDiv.append(divComments);
    addCommentForm(parentDiv, blog.id);
}
function gotoComments() {
    var scroll_offset = $("#commentArea").offset();
    $("body,html").animate({
        scrollTop: scroll_offset.top
    }, 20);
}
function putComments(parentDiv, comments) {
    parentDiv.empty();
    for (var i = 0; i < comments.length; i++) {
        var comment = comments[i];
        var divComment = $('<div style="padding-top: 10px;padding-left: 10px;padding-right: 10px"></div>');
        var floorNum = $('<span style="color:black;width: 40px">' + (i + 1) + '楼</span>');
        divComment.append(floorNum);
        divComment.append("　　");
        var username = $('<span class="glyphicon glyphicon-user" style="color: gray ;width:150px" aria-hidden="true">　' + comment.username + '</span>');
        divComment.append(username);
        divComment.append("　　　　");
        var email = $('<span class="glyphicon glyphicon-envelope" style="color: gray;width:300px" aria-hidden="true">　' + comment.email + '</span>');
        divComment.append(email);
        var divContent = $('<div style="font-size:16px;padding: 20px 50px 10px 45px;"></div>').text(comment.content);
        divComment.append(divContent);
        var divDate = $('<div style="color:gray;float: right"></div>');
        var date = $('<i></i>').text("评论于" + getSmpFormatDateByLong(comment.createTime, true));
        divDate.append(date);
        divComment.append(divDate);
        divComment.append('<hr>');
        parentDiv.append(divComment);
    }
}
function addCommentForm(parentDiv, id) {
    $('<form action="<%=path %>" method="post" id="commentform" name="commentform">' +
        '<div class="form-group">' +
        ' <label for="username">用户名</label>' +
        '<input type="text" class="form-control" name="username" id="username" placeholder="请输入用户名">' +
        '</div>' +
        '<div class="form-group">' +
        '<label for="email">邮箱</label>' +
        '<input type="text" class="form-control" name="email" id="email" placeholder="请输入邮箱">' +
        '</div>' +
        '<div class="form-group">' +
        '' +
        '<label for="content">文本框</label>' +
        '<textarea class="form-control" id="content" name="content" rows="5"></textarea>' +
        '</div>' +
        ' <input name="blogId" type="hidden" id="blogId" value="' + id + '">' +
        '<input type="button" onclick="checkInput(' + id + ')" value="提交">' +
        '</form>').appendTo(parentDiv);
}
function putBlogs(parentDiv, blogs) {
    parentDiv.empty();
    for (var i = 0; i < blogs.length; i++) {
        ///////////////////标题部分////////////////////
        var titleHref = $('<a href="javascript:void(0);" onclick=getBlogById(' + blogs[i].id + ')></a>');
        var title = $('<h1 class="page-header"></h1>').text(blogs[i].title);
        titleHref.append(title);
        parentDiv.append(titleHref);
        ////////////////////////部分正文及封面部分///////////////////////////
        var divCenter = $('<div class="row" style="height: 150px;"></div>');
        var divImg = $('<div class="col-md-4 col-lg-4 col-sm-6 col-xs-6 left"></div>');
        var img = $('<img class="img-responsive img-thumbnail" style="height: 140px;width:140px"/>');
        //console.log(blogs[i].picUrl);
        img.attr('src', blogs[i].picUrl);
        //img.src=blogs[i].picUrl
        //console.log(img.src);
        divImg.append(img);
        var divContent = $('<div class="right" style="word-break:break-all; word-wrap:break-word"></div>').text(blogs[i].content.substr(0, 40));
        divCenter.append(divImg);
        divCenter.append(divContent);
        parentDiv.append(divCenter);
        //////////////////////////底部其他信息部分////////////////////////////////////////////////
        var divBottom = $('<div class="row"></div>');
        var divDate = $('<div class="col-md-3 col-lg-3 col-sm-3 col-xs-3 left"></div>');
        var labelDate = $('<label style="font-size: 12px;"></label>').text(getSmpFormatDateByLong(blogs[i].createTime, true));
        divDate.append(labelDate);
        var divClickTimes = $('<div class="col-md-3 col-lg-3 col-sm-3 col-xs-3 left"></div>');
        var iconClickTimes = $('<span class="glyphicon glyphicon-eye-open" style="color: gray" aria-hidden="true"></span>');
        var hrefClickTimes = $('<a style="text-decoration:none;" href="#"></a>').text("阅读");
        iconClickTimes.append(hrefClickTimes);
        iconClickTimes.append("（" + blogs[i].clickTimes + "）");
        //iconClickTimes.text(blogs[i].clickTimes);
        divClickTimes.append(iconClickTimes);
        var divCommentTimes = $('<div class="col-md-3 col-lg-3 col-sm-3 col-xs-3 left"></div>');
        var iconCommentTimes = $('<span class="glyphicon glyphicon-edit" style="color: gray" aria-hidden="true"></span>');
        var hrefCommentTimes = $('<a style="text-decoration:none;" href="javascript:void(0);" onclick=getBlogByIdAndGoToComments(' + blogs[i].id + ')></a>').text("评论");
        iconCommentTimes.append(hrefCommentTimes);
        iconCommentTimes.append("（" + blogs[i].commentTimes + "）");
        divCommentTimes.append(iconCommentTimes);
        var divAgreeTimes = $('<div class="col-md-3 col-lg-3 col-sm-3 col-xs-3 left"></div>');
        var buttonAgree = $('<button class="btn"></button>');
        buttonAgree.attr('onclick', "addagree(" + blogs[i].id + ")");
        var iconAgreeTimes = $('<span class="glyphicon glyphicon-thumbs-up button" style="color: gray" aria-hidden="true"></span>');
        var labelAgreeTimes = $('<label style="text-align: left"></label>').text("推荐(" + blogs[i].agreeWithTimes + ")　");
        labelAgreeTimes.attr('id', "agree" + blogs[i].id);
        buttonAgree.append(iconAgreeTimes);
        divAgreeTimes.append(buttonAgree);
        divAgreeTimes.append(labelAgreeTimes);
        divBottom.append(divDate);
        divBottom.append(divClickTimes);
        divBottom.append(divCommentTimes);
        divBottom.append(divAgreeTimes);
        parentDiv.append(divBottom);

    }
}
function putPageSelector(parentDiv, pageCount, pageNum) {
    var pageSelector = $('<ul class="pagination"></ul>');
    var firstPage = $('<li><a href="javascript:void(0);">&laquo;</a></li>');
    firstPage.attr("onclick", 'getBlogsByPageNum(' + 1 + ')');
    pageSelector.append(firstPage);
    for (var i = (pageNum - 3 < 1 ? 1 : pageNum - 3); i <= (pageCount > pageNum + 3 ? pageNum + 3 : pageCount); i++) {
        if (i == pageNum) {
            var active = $('<li class="active"></li>');
            var hrefActive = $('<a href="javascript:void(0);"></a>').text(i);
            hrefActive.attr("onclick", 'getBlogsByPageNum(' + i + ')');
            active.append(hrefActive);
            pageSelector.append(active);
        }
        else {
            var otherPage = $('<li></li>');
            var hrefOtherPage = $('<a href="javascript:void(0);"></a>').text(i);
            hrefOtherPage.attr("onclick", 'getBlogsByPageNum(' + i + ')');
            otherPage.append(hrefOtherPage);
            pageSelector.append(otherPage);
        }
    }
    var lastPage = $('<li><a href="javascript:void(0);">&raquo;</a></li>');
    lastPage.attr("onclick", 'getBlogsByPageNum(' + pageCount + ')');
    pageSelector.append(lastPage);
    parentDiv.append(pageSelector);
}
//扩展Date的format方法
Date.prototype.format = function (format) {
    var o = {
        "M+": this.getMonth() + 1,
        "d+": this.getDate(),
        "h+": this.getHours(),
        "m+": this.getMinutes(),
        "s+": this.getSeconds(),
        "q+": Math.floor((this.getMonth() + 3) / 3),
        "S": this.getMilliseconds()
    }
    if (/(y+)/.test(format)) {
        format = format.replace(RegExp.$1, (this.getFullYear() + "").substr(4 - RegExp.$1.length));
    }
    for (var k in o) {
        if (new RegExp("(" + k + ")").test(format)) {
            format = format.replace(RegExp.$1, RegExp.$1.length == 1 ? o[k] : ("00" + o[k]).substr(("" + o[k]).length));
        }
    }
    return format;
}
/**
 *转换日期对象为日期字符串
 * @param date 日期对象
 * @param isFull 是否为完整的日期数据,
 *               为true时, 格式如"2000-03-05 01:05:04"
 *               为false时, 格式如 "2000-03-05"
 * @return 符合要求的日期字符串
 */
function getSmpFormatDate(date, isFull) {
    var pattern = "";
    if (isFull == true || isFull == undefined) {
        pattern = "yyyy-MM-dd hh:mm:ss";
    } else {
        pattern = "yyyy-MM-dd";
    }
    return getFormatDate(date, pattern);
}
/**
 *转换当前日期对象为日期字符串
 * @param date 日期对象
 * @param isFull 是否为完整的日期数据,
 *               为true时, 格式如"2000-03-05 01:05:04"
 *               为false时, 格式如 "2000-03-05"
 * @return 符合要求的日期字符串
 */

function getSmpFormatNowDate(isFull) {
    return getSmpFormatDate(new Date(), isFull);
}
/**
 *转换long值为日期字符串
 * @param l long值
 * @param isFull 是否为完整的日期数据,
 *               为true时, 格式如"2000-03-05 01:05:04"
 *               为false时, 格式如 "2000-03-05"
 * @return 符合要求的日期字符串
 */

function getSmpFormatDateByLong(l, isFull) {
    return getSmpFormatDate(new Date(l), isFull);
}
/**
 *转换long值为日期字符串
 * @param l long值
 * @param pattern 格式字符串,例如：yyyy-MM-dd hh:mm:ss
 * @return 符合要求的日期字符串
 */

function getFormatDateByLong(l, pattern) {
    return getFormatDate(new Date(l), pattern);
}
/**
 *转换日期对象为日期字符串
 * @param l long值
 * @param pattern 格式字符串,例如：yyyy-MM-dd hh:mm:ss
 * @return 符合要求的日期字符串
 */
function getFormatDate(date, pattern) {
    if (date == undefined) {
        date = new Date();
    }
    if (pattern == undefined) {
        pattern = "yyyy-MM-dd hh:mm:ss";
    }
    return date.format(pattern);
}