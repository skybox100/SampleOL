<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
<!DOCTYPE html>
<html>
<head>
<meta charset="UTF-8">
<title>Insert title here</title>
</head>
<body>
<select onchange="regimentSelectChange(this)">
	<option>전체</option>
	<option value="28여단">28여단</option>
	<option value="28-1대대">28-1대대</option>
	<option value="28-2대대">28-2대대</option>
	<option value="28-3대대">28-3대대</option>
</select>

<select id="regim_company">
<option>전체</option>
</select>
<script>
function regimentSelectChange(e) {
	
	var rc0 = ['전체', '28여단 본부', '28여단 수송대','28여단 지원중대','28여단 수색중대','28여단 통신중대','28여단 의무중대'];
	var rc1 = ['전체', '28-1대대 본부', '28-1대대 1중대','28-1대대 2중대','28-1대대 3중대','28-1대대 4중대','28-1대대 의무중대'];  
	var rc2 = ['전체', '28-2대대 본부', '28-2대대 5중대','28-2대대 6중대','28-2대대 7중대','28-2대대 8중대'];
	var rc3 = ['전체', '28-3대대 본부', '28-3대대 9중대','28-3대대 10중대','28-3대대 11중대','28-3대대 12중대', '28-3대대 의무중대'];
	
	var target = document.getElementById("regim_company");

	if(e.value == "28여단") var d = rc0;
	else if(e.value == "28-1대대") var d = rc1;
	else if(e.value == "28-2대대") var d = rc2;
	else if(e.value == "28-3대대") var d = rc3;

	target.options.length = 0;

	for (x in d) {
		var opt = document.createElement("option");
		opt.value = d[x];
		opt.innerHTML = d[x];
		target.appendChild(opt);
	}
}
</script>
</body>
</html>