<%@page isELIgnored="false"%>
<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<c:set var="ctx" value="${pageContext.request.contextPath}"/>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html xmlns="http://www.w3.org/1999/xhtml">
<head>
<meta http-equiv="Content-Type" content="text/html; charset=utf-8" />
<link href="${ctx}/monitor/css/style-zibiao.css"  rel="stylesheet" type="text/css"/>
<script type="/text/javascript" src="${ctx}/monitor/js/table.js"></script>


<title>营销员业绩助理报表口径说明</title>
</head>

<body>
<table width="100%" border="0" cellspacing="0" cellpadding="0" class="table1" id="table">
      <tr>
	     <th align="center" width="40">大类</th>
		 <th align="center" width="50">界面位置</th>
		 <th align="center">指标</th>
		 <th align="center">说明</th>
	  </tr>
	  <tr>
	      <td align="center">标题类</td>
		  <td align="center">title</td>
		  <td align="left">日期</td>
		  <td align="left">默认当前日期。可回查30天内业绩，查历史为历史日期最后一次统计数据。</td>
	  </tr>
	  <tr>
	      <td align="center">标题类</td>
		  <td align="center">title</td>
		  <td align="left">数据更新时间</td>
		  <td align="left">显示最新一次数据统计更新时间。</td>
	  </tr>
	  <tr>
	      <td align="center">业绩区</td>
		  <td align="center">上-左</td>
		  <td align="left">交表量（当日首拨）</td>
		  <td align="left">截止至数据统计时间为止，数据首次拨打时间为当天，且当天报批的营销员本人的电子表单量。</td>
	  </tr>
      <tr>
	      <td align="center">业绩区</td>
		  <td align="center">上-左</td>
		  <td align="left">交表量（跟进）</td>
		  <td align="left">截止至数据统计时间为止，数据首次拨打时间为非当天的，且当天报批的营销员本人的电子表单量。</td>
	  </tr>
      <tr>
	      <td align="center">业绩区</td>
		  <td align="center">上-左</td>
		  <td align="left">进件量（当日首拨）</td>
		  <td align="left">截止至数据统计时间为止，数据首次拨打时间为当天、且组长审核通过的时间为当天的营销员本人的电子表单量。</td>
	  </tr>
      <tr>
	      <td align="center">业绩区</td>
		  <td align="center">上-左</td>
		  <td align="left">进件量（跟进）</td>
		  <td align="left">当天组长审核通过的营销员本人的电子表单中除去“进件量（当日首拨）”外的电子表单总量。</td>
	  </tr>
      <tr>
	      <td align="center">业绩区</td>
		  <td align="center">上-中</td>
		  <td align="left">激励区</td>
		  <td align="left">以三行文字显示当前营销员截止至数据统计时间为止，与营销员所在小组、大区、中心的进件冠军（显示冠军姓名及进件量）相比的差距。</td>
	  </tr>
      <tr>
	      <td align="center">业绩区</td>
		  <td align="center">上-右</td>
		  <td align="left">近6天业绩排名趋势图（本组、大区、中心）</td>
		  <td align="left">以折线图展示当前营销员历史5天及今天在本小组、大区、中心的进件表量排名趋势，历史天的排名数据统计到当天23:59:59,今天的截止数据统计时间为准。</td>
	  </tr>
      <tr>
	      <td align="center">数据区</td>
		  <td align="center">中-左</td>
		  <td align="left">当天首拨</td>
		  <td align="left">截止至数据统计时间为止，营销员当天获取/执行且当天拨打的（除延伸跟进数据外）数据总量。</td>
	  </tr>
      <tr>
	      <td align="center">数据区</td>
		  <td align="center">中-左</td>
		  <td align="left">当天配额</td>
		  <td align="left">营销员当天的配额总量。</td>
	  </tr>
      <tr>
	      <td align="center">数据区</td>
		  <td align="center">中-左</td>
		  <td align="left">当天处理跟进量（非延伸数据）</td>
		  <td align="left">截止到前一天23:59:59为待跟进的数据当天被执行的数据总量。<br />
		  界面中显示为数值+"F",如35F，表示此数据有35条。</td>
	  </tr>
	   <tr>
	      <td align="center">数据区</td>
		  <td align="center">中-左</td>
		  <td align="left">当天处理跟进量（延伸数据）</td>
		  <td align="left">截止到前一天23:59:59为延伸跟进数据当天被执行的数据总量。<br />
                            界面中显示为数值+"MGL",如35延伸，表示此数据有35条。</td>
	  </tr>
      <tr>
	      <td align="center">数据区</td>
		  <td align="center">中-左</td>
		  <td align="left">待跟进量（非延伸数据）</td>
		  <td align="left">截止到前一天23:59:59为延伸跟进数据总量。<br />
                               界面中显示为数值+"MGL",如35延伸，表示此数据有35条。</td>
	  </tr>
	  <tr>
	      <td align="center">数据区</td>
		  <td align="center">中-左</td>
		  <td align="left">待跟进量（延伸数据）</td>
		  <td align="left">截止到前一天23:59:59为延伸跟进数据总量。<br />
                                           界面中显示为数值+"MGL",如35延伸，表示此数据有35条。</td>
	  </tr>
      <tr>
	      <td align="center">数据区</td>
		  <td align="center">中-左</td>
		  <td align="left">预约当天完成量（非延伸数据）</td>
		  <td align="left">截止到前一天23:59:59预约时间为当天待跟进数据且当天被执行的数据总量。<br />
                                                           界面中显示为数值+"F",如35F，表示此数据有35条。</td>
	  </tr>
	  <tr>
	      <td align="center">数据区</td>
		  <td align="center">中-左</td>
		  <td align="left">预约当天完成量（延伸数据）</td>
		  <td align="left">截止到前一天23:59:59预约时间为当天延伸跟进数据且当天被执行的数据总量。<br />
                                                     界面中显示为数值+"MGL",如35延伸，表示此数据有35条。。</td>
	  </tr>
      <tr>
	      <td align="center">数据区</td>
		  <td align="center">中-左</td>
		  <td align="left">预约当天跟进量（非延伸数据）</td>
		  <td align="left">截止到前一天23:59:59预约时间为当天待跟进数据量。<br />
                                                           界面中显示为数值+"F",如35F，表示此数据有35条。。</td>
	  </tr>
	  <tr>
	      <td align="center">数据区</td>
		  <td align="center">中-左</td>
		  <td align="left">预约当天跟进量（延伸数据）</td>
		  <td align="left">截止到前一天23:59:59预约时间为当天延伸跟进数据量。<br />
                                          界面中显示为数值+"MGL",如35延伸，表示此数据有35条。</td>
	  </tr>
      <tr>
	      <td align="center">数据区</td>
		  <td align="center">中-中</td>
		  <td align="left">5天内被回收数据总量</td>
		  <td align="left">历史5天内被回收的数据总量，历史5天内因超过数据有效期（回收期）的待跟进数据被系统回收的数据总量。</td>
	  </tr>
      <tr>
	      <td align="center">数据区</td>
		  <td align="center">中-中</td>
		  <td align="left">当天到期数据总量</td>
		  <td align="left">当天不处理则将要被回收的各类数据总量。</td>
	  </tr>
      <tr>
	      <td align="center">数据区</td>
		  <td align="center">中-中</td>
		  <td align="left">未来第二天到期数据量</td>
		  <td align="left">未来第二天再不处理则将要被回收的各类数据总量。</td>
	  </tr>
      <tr>
	      <td align="center">数据区</td>
		  <td align="center">中-中</td>
		  <td align="left">未来第三天到期数据量</td>
		  <td align="left">未来第三天再不处理则将要被回收的各类数据总量。</td>
	  </tr>
      <tr>
	      <td align="center">数据区</td>
		  <td align="center">中-中</td>
		  <td align="left">未来第四天到期数据量</td>
		  <td align="left">未来第四天再不处理则将要被回收的各类数据总量。</td>
	  </tr>
      <tr>
	      <td align="center">数据区</td>
		  <td align="center">中-中</td>
		  <td align="left">未来第五天到期数据量</td>
		  <td align="left">未来第五天再不处理则将要被回收的各类数据总量。</td>
	  </tr>
      <tr>
	      <td align="center">数据区</td>
		  <td align="center">中-右</td>
		  <td align="left">最近5天推荐数据趋势图<br />
A推B（五天前）<br />
A推B（四天前）<br />
A推B（三天前）<br />
A推B（两天前）<br />
A推B（昨天）<br />
A推B（今天）<br />
B推C（五天前）<br />
B推C（四天前）<br />
B推C（三天前）<br />
B推C（两天前）<br />
B推C（昨天）<br />
B推C（今天）<br />
</td>
		  <td align="left" valign="middle">以折线图展示当前营销员历史5天及今天推荐数据总量。</td>
	  </tr>
      <tr>
	      <td align="center">话务区</td>
		  <td align="center">下</td>
		  <td align="left">昨天总计</td>
		  <td align="left">昨天截至数据统计时间的成功通时、非成功通时、话后处理（时间）、小休时长、等待时长、振铃时长各总和。如本次数据统计时间为2017-03-24 15:00:00，则昨天的数据统计时间为2017-03-23 15:00:00。</td>
	  </tr>
      <tr>
	      <td align="center">话务区</td>
		  <td align="center">下</td>
		  <td align="left">今天总计</td>
		  <td align="left">截至数据统计时间的成功通时、非成功通时、话后处理（时间）、小休时长、等待时长、振铃时长各总和。</td>
	  </tr>
      <tr>
	      <td align="center">话务区</td>
		  <td align="center">下</td>
		  <td align="left">成功通时</td>
		  <td align="left">各时段下成功标的通时（从客户接听电话到挂机时长）总和，如一通成功标的电话时间区间9:45:00—11:05:00，则此通电话9:00-9:59点段的成功通时15:00、10:00-10:59段的成功通时为60:00、11:00-12:59点段的成功通时为05:00。</td>
	  </tr>
      <tr>
	      <td align="center">话务区</td>
		  <td align="center">下</td>
		  <td align="left">非成功通时</td>
		  <td align="left">各时段所下标识为非成功标识的通时（从客户接听电话到挂机时长）总和，如一通非成功标的电话时间区间9:45:00—11:05:00，则此通电话9:00-9:59点段的非成功通时15:00、10:00-10:59段的非成功通时为60:00、11:00-12:59点段的非成功通时为05:00。</td>
	  </tr>
      <tr>
	      <td align="center">话务区</td>
		  <td align="center">下</td>
		  <td align="left">话后处理</td>
		  <td align="left">挂机后到点击启通中【完成】按钮的时长，若未点击启通中【完成】按钮，则为挂机后到获取下一条数据的时长。</td>
	  </tr>
      <tr>
	      <td align="center">话务区</td>
		  <td align="center">下</td>
		  <td align="left">小休时长</td>
		  <td align="left">启通宝进入小休状态的时长，由营销员在启通宝中点击【小休】按钮触发小休状态。</td>
	  </tr>
      <tr>
	      <td align="center">话务区</td>
		  <td align="center">下</td>
		  <td align="left">等待时长</td>
		  <td align="left">启通宝处理等待状态的时长，小休状态点击启通宝【工作】按钮后未点击启通宝其它按钮也未拨号则进入等待状态、话后处理后未点击启通宝其它按钮也未拨号则进入等待状态。</td>
	  </tr>
      <tr>
	      <td align="center">话务区</td>
		  <td align="center">下</td>
		  <td align="left">振铃时长</td>
		  <td align="left">客户电话振铃时长。</td>
	  </tr>
      <tr>
	      <td align="center">话务区</td>
		  <td align="center">下</td>
		  <td align="left">通话次数</td>
		  <td align="left">营销员每一次拨号，接通通话的数量。空则显示为0。</td>
	  </tr>
      <tr>
	      <td align="center">话务区</td>
		  <td align="center">下</td>
		  <td align="left">未通话次数</td>
		  <td align="left">营销员每一次拨号，未通话的数量。空则显示为0。</td>
	  </tr>
      <tr>
	      <td align="center">话务区</td>
		  <td align="center">下</td>
		  <td align="left">数据接触量</td>
		  <td align="left">营销员数据，执行了“保存”操作的数量。空则显示为0。例如，数据A，营销员甲在7点半通话后，操作“保存并跟进”。甲在8点45分再次对该数据A进行“保存并跟进”。在10点15分操作“保存”。此情况在本字段中，将分别在07:00-07:59，08:00-08:59，10:00-10:59各记录1次。</td>
	  </tr>
      <tr>
	      <td align="center">话务区</td>
		  <td align="center">下</td>
		  <td align="left">库存量</td>
		  <td align="left">将营销员名下的“项目客户跟进列表”、“延伸客户跟进列表”数量相加。在“昨天统计”一栏，显示所选日期前一天23:00-23:59的数据。在“今天总计”一栏，显示所选日期最新的数据，非当天每个时间段的总和。每天最后一次统计历史100天，其余统计历史60天的数据。</td>
	  </tr>
 </table>
</body>
</html>
