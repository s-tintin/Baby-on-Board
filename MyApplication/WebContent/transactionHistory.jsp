<!DOCTYPE html>
<%@page import="db.DbManager"%>
<%@page import="java.sql.Connection"%>
<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
<html lang="en" style="-webkit-text-size-adjust: 100%; -ms-text-size-adjust: 100%;">
    <head>
        <meta charset="utf-8" />
        <meta name="viewport" content="width=device-width initial-scale=1.0 maximum-scale=1.0 user-scalable=yes" />
        <meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
        <!--LINKS-->
        
        <link href="https://fonts.googleapis.com/css?family=Lato:100,300,300i,400" rel="stylesheet">        
		<link rel="stylesheet" type="text/css" href="css/transactionHistory.css">
	    <link rel="stylesheet" href="https://cdnjs.cloudflare.com/ajax/libs/font-awesome/4.7.0/css/font-awesome.min.css">
	    <link rel="stylesheet" href="https://cdnjs.cloudflare.com/ajax/libs/normalize/5.0.0/normalize.min.css">
		<link rel='stylesheet prefetch' href='https://fonts.googleapis.com/css?family=Overpass:300,400,600,800'>
		<link rel="stylesheet" href="css/tab-style.css">
		<!-- Scroll to top on reload -->
		<script type="application/x-javascript"> addEventListener("load", function() { setTimeout(hideURLbar, 0); }, false);
				function hideURLbar(){ window.scrollTo(0,1); } </script>
				
		<title>Baby On Board | Transaction History</title>
    </head>
	<body>
	
		<!-- Login Validation -->
		<script type="text/javascript">
		    	var loginStatus;
			    var loginStatus = <%=session.getAttribute("loginStatus")%>;
			    var contextPath = "<%=request.getContextPath()%>";
			    
			    // Proceed to logout if user not logged in
			    if(loginStatus != true){
			    	var logout_url =  window.location.protocol + "//" + window.location.hostname + ":" + window.location.port + contextPath + "/logout";
					window.location.href = logout_url;
			    }
			    
			    // Get customer details from session
			    var user = <%=session.getAttribute("customerDetails")%>;
		</script>
		
		<!-- Overlay -->
		<div id="overlay" class="hide">
			<div class="overlay-content"><i class="fa fa-spinner fa-spin"></i> Updating cart...</div>
		</div>
		
		<!--HEADER SECTION -->
		<div class="header">
			<a id="logo-link" href="/index"> <img src="images/baby.png"
				alt="logo" class="logo">
			</a>
			<ul class="main-nav">
				<li><a href="#features">FEATURES</a></li>
				<li><a href="#work">HOW IT WORKS</a></li>
				<li><a href="#contact">CONTACT</a></li>
				<li><button id="user-button" class="user">
						<i class="fa fa-user-circle-o"
							style="font-size: 30px; color: #fff;"></i>
						<div class="user_dropdown ">
							<ul class="dropdown_nav">
								<li><a href="/userProfile" id="user_profile">User
										profile</a></li>
								<li><a id="transaction" href="/transactionHistory">Transaction
										history</a></li>
								<li><a href="/logout" id="logout_button">Logout</a></li>
							</ul>
						</div>
	
					</button></li>
				<li><button id="cart_btn" class="cart_btn_class"><span class="count" id="cart-count">0</span><i class="fa fa-shopping-cart cart" style="font-size:30px;color:white"></i></button>
			</ul>
		</div>
		
		<!-- CART MODAL -->
		<div id="cart-modal" class="modal">
			<div class="modal-content">
				<span class="close">&times;</span>
				<table class="cart-table">
					<thead>
						<tr>
							<th>Item No</th>
							<th>Age Group</th>
							<th>Subscription Name</th>
							<th>Quantity</th>
							<th>Price</th>
							<th>Delete</th>
						</tr>
					</thead>
					<tbody id="cart-table1">
					</tbody>
				</table>
				<div class="cartCheckout">
					<button class="cartCheckout_btn ">Checkout</button>
				</div>
			</div>
		</div>
		
		<!-- Active Subscriptions and controllers -->
		<div class="tabset">
			<!-- Tab 1 -->
			<input type="radio" name="tabset" id="tab1"
				aria-controls="Subscriptions" checked> <label for="tab1" id="subscription-label">Active
				Subscription</label>
			<!-- Tab 2 -->
			<input type="radio" name="tabset" id="tab2"
				aria-controls="Transaction"> <label for="tab2" id="transaction-label">Transaction
				History</label>
	
			<div class="tab-panels">
				<section id="Subscriptions" class="tab-panel">
					<h2>Active Subscriptions</h2>
				</section>
				<section id="Transaction" class="tab-panel">
					<h2>Transaction History</h2>
				</section>
			</div>
		</div>
		
		<!-- POP UP MESSAGE -->
        <div id="pop-up-message">
		</div>
		
		<!--JAVA SCRIPTS -->
		<script type="text/javascript" src="js/transactionHistory.js"></script>
	</body>
</html>
    