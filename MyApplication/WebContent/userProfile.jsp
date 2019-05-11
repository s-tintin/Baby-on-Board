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
		<link rel="stylesheet" type="text/css" href="css/userProfile.css">
	    <link rel="stylesheet" href="https://cdnjs.cloudflare.com/ajax/libs/font-awesome/4.7.0/css/font-awesome.min.css">
		<!-- Scroll to top on reload -->
		<script type="application/x-javascript"> addEventListener("load", function() { setTimeout(hideURLbar, 0); }, false);
				function hideURLbar(){ window.scrollTo(0,1); } </script>
				
		<title>Baby On Board | User Profile</title>
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
            <a id="logo-link" href="/index"> <img src="images/baby.png" alt="logo" class="logo"> </a>
            <ul class="main-nav">
                    <li><a href="#features">FEATURES</a></li>
                    <li><a href="#work">HOW IT WORKS</a></li>
                    <li><a href="#contact">CONTACT</a></li>                           
                    <li><button id="user-button" class="user"><i class="fa fa-user-circle-o" style="font-size:30px;color:#fff;"></i>
                    <div class = "user_dropdown ">
						<ul class="dropdown_nav" >
							<li><a href="/userProfile" id="user_profile">User profile</a></li>
							<li><a id="transaction" href="/transactionHistory">Transaction history</a></li>
							<li><a href="/logout" id="logout_button">Logout</a></li>
						</ul>
						</div>
                    
                    </button></li>
                    <li><button id="cart_btn" class="cart_btn_class"><span class="count" id="cart-count">0</span><i class="fa fa-shopping-cart cart" style="font-size:30px;color:white"></i></button>
                             
            </ul>
        </div>
    	
    	<!-- Customer Form -->
    	<h1 id="user-heading" class="te-al-ce">
    		<img class="user-image" src="images/human.png"/><div id="user-heading-name"></div>
    	</h1>
    	<form id="edit-customer" name="editcustomer" action="UpdateCustomer" method="post">
    		<div class="form-element hide"><label class="form-label">Id:</label><input type="text" name="id" class="form-value outline-none" placeholder="Id"></div>
    		<div class="form-element hide"><label class="form-label">Username:</label><input type="text" name="username" class="form-value outline-none" placeholder="Username"></div>
	        <div class="form-element"><label class="form-label">Full Name:</label><input type="text" name="name" class="form-value outline-none" placeholder="Full Name"></div>
	        <div class="form-element"><label class="form-label">Email Address:</label><input type="text" name="email" class="form-value outline-none"  placeholder="Email Address"></div>
	        <div class="form-element"><label class="form-label">Phone Number:</label><input type="text" name="phone" class="form-value outline-none" placeholder="Phone Number"></div>
	        <div class="form-element"><label class="form-label">Password:</label><input type="password" name="password" class="form-value outline-none" placeholder="Password"></div>
	        <div class="form-element"><label class="form-label">Retype Password:</label><input type="password" name="retry-password" class="form-value outline-none" placeholder="Confirm Password"></div>
	        <div class="form-element te-al-ce"><button type="submit" name="submit" class="form-submit bo-sh-rgba pointer outline-none" value="register">Save</button></div>
        </form>
    	
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
    
    	<!-- POP UP MESSAGE -->
        <div id="pop-up-message">
		</div>
    
    
    	<!--JAVA SCRIPTS -->
        <script type="text/javascript" src="js/userProfile.js"></script>
    </body>
</html>
    