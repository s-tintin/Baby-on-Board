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
		<link rel="stylesheet" type="text/css" href="css/index.css">
	    <link rel="stylesheet" href="https://cdnjs.cloudflare.com/ajax/libs/font-awesome/4.7.0/css/font-awesome.min.css">
		<!-- Scroll to top on reload -->
		<script type="application/x-javascript"> addEventListener("load", function() { setTimeout(hideURLbar, 0); }, false);
				function hideURLbar(){ window.scrollTo(0,1); } </script>
				
		<title>Baby On Board | Landing</title>
    </head>
    
    <body>
    
	    <!-- Login Validation -->
	    <script type="text/javascript">
	    	var loginStatus;
		    var loginStatus = <%=session.getAttribute("loginStatus")%>;
		    var errorMessage = "<%=session.getAttribute("errorMessage")%>";
		    var contextPath = "<%=request.getContextPath()%>";
		    
		 	// Get customer details from session
		    var user = <%=session.getAttribute("customerDetails")%>;
	    </script>
	    
		<!-- Database Connection -->
		<%
			DbManager db = new DbManager();
			Connection conn = (Connection)db.getConnection();
		%>
		
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
                    <li><button id="user-button" class="user hide"><i class="fa fa-user-circle-o" style="font-size:30px;color:#fff;"></i>
                    <div class = "user_dropdown ">
						<ul class="dropdown_nav" >
							<li><a href="/userProfile" id="user_profile">User profile</a></li>
							<li><a id="transaction" href="/transactionHistory">Transaction history</a></li>
							<li><a id="logout_button" href="/logout">Logout</a></li>
						</ul>
						</div>
                    
                    </button></li>
                    <li><button id="cart_btn" class="cart_btn_class hide"><span class="count" id="cart-count">0</span><i class="fa fa-shopping-cart cart" style="font-size:30px;color:white"></i></button>
                             
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
        
        <!--LANDING PAGE-->
        <div class="home">
        <!-- Login -->
           <form id="login" name="loginform" action="LoginController" method="post">
           		 <p id="incorrect-login" class="te-al-ce hide">Password invalid!</p>
                 <h1 style="text-align:center;"><img class="human" src="images/human.png"/> <br><span style="font-size:35px;">LOGIN </span><br></h1>
                 <input type="text" name="username" class="login outline-none" placeholder="Username">
                 <input type="password" name="password" class="login outline-none" placeholder="Password">
                 <button type="submit" class="login bo-sh-rgba pointer btn-1 outline-none" name="submit" value="login">Login</button>
                 <a class="hide" href="#"><h3 style="text-align:center;color:#555555;text-decoration:none;"> <br>Forgot Your Password? <br><br></h3></a>
                 <button id="sign-up-button" type="button" class="button outline-none" style="padding: 10px 56px;" > Not a registered user yet? Sign Up!</button>
           </form>
           <!-- Register -->
           <form id="register" name="regform" action="LoginController" method="post" >
           	 <p id="incorrect-register" class="te-al-ce hide">Login details invalid, register to continue!</p>
             <h1 class="te-al-ce"><img class="human reg" src="images/human.png"/> <br>REGISTER</h1>
             <input type="text" name="name" class="register outline-none" placeholder="Full Name">
             <input type="text" name="email" class="register outline-none"  placeholder="Email Address">
             <input type="text" name="phone" class="register outline-none" placeholder="Phone Number">
             <input type="text" name="username" class="register outline-none" placeholder="Username">
             <input type="password" name="password" class="register outline-none" placeholder="Password">
             <input type="password" name="retry-password" class="register outline-none" placeholder="Confirm Password">
             <button type="submit" name="submit" class="register bo-sh-rgba pointer btn-1 size1 outline-none" value="register">Register</button>
             <button id="login-button" type="button" class="button outline-none" style="padding: 15px 85px;" > Already registered? LOGIN</button>
           </form>
        </div>
        
        <!--AGE CATEGORY-->
        <div>
            <ul id="age-group-list" class="age">
                <h2>Choose a category</h2>
            </ul>
        </div>
        
        
        <!--SUBSCRIPTION MODEL-->
        <div id="subscription-container"> 
        </div>
        
        
        <!-- SUBSCRIPTION DURATION-->
         <div>            
            <ul class="subscription-duration">
                <h2> Choose the subscription Duration in Months</h2>
                <div class="border"></div>
  				<div class="duration_list">
	                <li>
	                  <button type="button" data-val="3" class="duration_button pointer"> 03</button>  
	                </li>
	                <li>
	                   <button type="button" data-val="6" class="duration_button pointer"> 06</button>
	                </li>
	                <li>
	                   <button type="button" data-val="9" class="duration_button pointer"> 09</button>
	                </li>
	                <li>
	                    <button type="button" data-val="12" class="duration_button pointer">12</button>
	                </li>
                </div>
            </ul>
        </div>
        
       	<!--  Proceed to checkout -->
       	<div class="checkout">
			<button class="checkout_btn">Proceed to Checkout</button>
       	</div>
       
       	<!-- POP UP MESSAGE -->
        <div id="pop-up-message">
		</div>
       
       <!--JAVA SCRIPTS -->
        <script type="text/javascript" src="js/index.js"></script>
    </body>
</html>
