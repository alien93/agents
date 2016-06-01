var agents = angular.module('agents', ['ngRoute','ngResource', 'ui.bootstrap']);


agents.config(
		function($routeProvider){
			$routeProvider
				.when(
						"/",
						{
							templateUrl: "agents.html"
						}
				)
				.when(
						"/ws",
						{
							templateUrl: "websocket.html"
						}
				)
		}
);