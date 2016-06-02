var ip = "192.168.90.199";
var ipMaster = "192.168.90.199";
var webSocket;


angular.module('agents')
			
		////////////////////////////////////////////////////////////////////
		/// 							REST							  //
		////////////////////////////////////////////////////////////////////
		.controller('AgentsController', ['$scope', '$http', '$uibModal',
		           function($scope, $http, $uibModal){
						
						////////////////////////////////////////////////////////////////////
						/// handshake - for non master nodes
						///////////////////////////////////////////////////////////////////
						var handshake = function(){
							var registerMe_data = {
													alias: "local_" + ip,
													address: ip
												  }
							$http.post("http://" + ipMaster + ":8080/AgentsWeb/rest/ac/node", registerMe_data);
						}
						
						////////////////////////////////////////////////////////////////////
						/// Check if I'm master 
						///////////////////////////////////////////////////////////////////
						$scope.onload = function(){
							$http.get("http://" + ip + ":8080/AgentsWeb/rest/ac/amIMaster")
								.success(function(data){
									if(data == 'true')
										console.log('I am master');
									else{
										console.log('Handshake...');
										handshake();
									}
								})
						};
						
	
						////////////////////////////////////////////////////////////////////
						/// Adding agent
						///////////////////////////////////////////////////////////////////
						$scope.addAgent = function(agent){
							var modalInstance = $uibModal.open({
								animation: false,
								templateUrl: 'agentName.html',
								controller: 'AgentNameController',
								resolve:{
									agent: function(){
										return agent;
									}
								}
							})
						}
						
						////////////////////////////////////////////////////////////////////
						/// Retreiving running agents
						///////////////////////////////////////////////////////////////////
						$scope.getRunningAgents = function(){
							$http.get("http://" + ip + ":8080/AgentsWeb/rest/agents/running").
							success(function(data){
								$scope.runningAgents = data;
							});
						};
						
						
						////////////////////////////////////////////////////////////////////
						/// Retreiving agent types
						///////////////////////////////////////////////////////////////////
						$scope.getAgentTypes = function(){
							$http.get("http://" + ip + ":8080/AgentsWeb/rest/agents/classes").
								success(function(data){
									$scope.agentTypes = data.agentTypes;
								});
						};
						
						
						////////////////////////////////////////////////////////////////////
						/// Retreiving performatives
						///////////////////////////////////////////////////////////////////
						$http.get("http://" + ip + ":8080/AgentsWeb/rest/messages").
							success(function(data){
								$scope.performatives = data;
							})
							
							
						////////////////////////////////////////////////////////////////////
						/// Sending messages
						///////////////////////////////////////////////////////////////////
						$scope.sendMessage = function(){
							
							var data = {
										"performative":
										$scope.selectedPerformative,
										"sender":
										$scope.selectedSender.id,
										"receivers":
										[$scope.selectedReciever.id],
										"replyTo":
										$scope.selectedReplyTo.id,
										"content":
										$scope.content,
										"contentObject":
										{},
										"userArgs":
										{},
										"language":
										$scope.language,
										"encoding":
										$scope.encoding,
										"ontology":
										$scope.ontology,
										"protocol":
										$scope.protocol,
										"conversationId":
										$scope.conversationId,
										"replyWith":
										$scope.replyWith,
										"replyBy":
										parseInt($scope.replyBy)
							}
							$http.post("http://" + ip + ":8080/AgentsWeb/rest/messages", data);
							
						}
						
						////////////////////////////////////////////////////////////////////
						/// Console output
						///////////////////////////////////////////////////////////////////
						$scope.getConsoleMessages = function(){
							$http.get("http://" + ip + ":8080/AgentsWeb/rest/messages/loggerMessages").
								success(function(data){
									$scope.consoleMessages = data;
									if(!$scope.$$phase) {
										$scope.$apply();
									}
								})
						}
						
						////////////////////////////////////////////////////////////////////
						/// Clearing console
						///////////////////////////////////////////////////////////////////
						$scope.clearConsole = function(){
							$http.post("http://" + ip + ":8080/AgentsWeb/rest/messages/loggerMessages")
							$scope.consoleMessages = [];
						}
						
							
						setInterval($scope.getRunningAgents, 2000);
						setInterval($scope.getAgentTypes,2000)
						setInterval($scope.getConsoleMessages, 2000);
						
		}
		])
		
		.controller('WebSocketController', ['$scope', '$http', '$uibModal',
		           function($scope, $http, $uibModal){
						
			
						////////////////////////////////////////////////////////////////////
						/// Creating websocket
						///////////////////////////////////////////////////////////////////
						if(webSocket == undefined)
							webSocket = new WebSocket("ws://" + ip + ":8080/AgentsWeb/websocket")
			
						////////////////////////////////////////////////////////////////////
						/// handshake - for non master nodes
						///////////////////////////////////////////////////////////////////
						var handshake = function(){
							var registerMe_data = {
													alias: "local_" + ip,
													address: ip
												  }
							$http.post("http://" + ipMaster + ":8080/AgentsWeb/rest/ac/node", registerMe_data);
						}
						
						////////////////////////////////////////////////////////////////////
						/// Check if master
						///////////////////////////////////////////////////////////////////
						$scope.onload = function(){
							$http.get("http://" + ip + ":8080/AgentsWeb/rest/ac/amIMaster")
								.success(function(data){
									if(data == 'true')
										console.log('I am master');
									else{
										console.log('Handshake...');
										handshake();
									}
								})
						};
						
	
						////////////////////////////////////////////////////////////////////
						/// Adding agents
						///////////////////////////////////////////////////////////////////
						$scope.addAgent = function(agent){
							var modalInstance = $uibModal.open({
								animation: false,
								templateUrl: 'agentName.html',
								controller: 'AgentNameWSController',
								resolve:{
									agent: function(){
										return agent;
									}
								}
							})
						}
						
						////////////////////////////////////////////////////////////////////
						/// Retreiving running agents
						///////////////////////////////////////////////////////////////////
						$scope.getRunningAgents = function(){
							if(webSocket.readyState == 1){
								var text = {"type": "RUNNING_AGENTS"};
								webSocket.send(JSON.stringify(text));
							}
						};
						
						
						////////////////////////////////////////////////////////////////////
						/// Retreiving agent types
						///////////////////////////////////////////////////////////////////
						$scope.getAgentTypes = function(){
							if(webSocket.readyState == 1){
								var text = {"type": "AGENT_TYPES"};
								webSocket.send(JSON.stringify(text));
							}
						};
						
						////////////////////////////////////////////////////////////////////
						/// Retreiving performatives
						///////////////////////////////////////////////////////////////////
						$scope.getPerformative = function(){
							if(webSocket.readyState == 1){
								var text = {"type":"PERFORMATIVES"};
								webSocket.send(JSON.stringify(text));
							}
						}
						
						////////////////////////////////////////////////////////////////////
						/// Sending messages
						///////////////////////////////////////////////////////////////////	
						$scope.sendMessage = function(){
														
							var data = {
										"performative":
										$scope.selectedPerformative,
										"sender":
										$scope.selectedSender.id,
										"receivers":
										[$scope.selectedReciever.id],
										"replyTo":
										$scope.selectedReplyTo.id,
										"content":
										$scope.content,
										"contentObject":
										{},
										"userArgs":
										{},
										"language":
										$scope.language,
										"encoding":
										$scope.encoding,
										"ontology":
										$scope.ontology,
										"protocol":
										$scope.protocol,
										"conversationId":
										$scope.conversationId,
										"replyWith":
										$scope.replyWith,
										"replyBy":
										parseInt($scope.replyBy)
							}
							if(webSocket.readyState == 1){
								var message = {"type":"SEND_MESSAGE", "data": data}
								webSocket.send(JSON.stringify(message));
							}
							
						}
						
						////////////////////////////////////////////////////////////////////
						/// Console output
						///////////////////////////////////////////////////////////////////
						$scope.getConsoleMessages = function(){
							$http.get("http://" + ip + ":8080/AgentsWeb/rest/messages/loggerMessages").
								success(function(data){
									$scope.consoleMessages = data;
									if(!$scope.$$phase) {
										$scope.$apply();
									}
								})
						}
						
						////////////////////////////////////////////////////////////////////
						/// Clearing console
						///////////////////////////////////////////////////////////////////
						$scope.clearConsole = function(){
							$http.post("http://" + ip + ":8080/AgentsWeb/rest/messages/loggerMessages")
							$scope.consoleMessages = [];
						}
						
						////////////////////////////////////////////////////////////////////
						/// Retreive agent types, performatives and runnning agents on socket open
						///////////////////////////////////////////////////////////////////
						webSocket.onopen = function(){
							$scope.getAgentTypes();
							$scope.getPerformative();
							$scope.getRunningAgents();
						}
						
						////////////////////////////////////////////////////////////////////
						/// Handling messages - retreiving running agents, types, performatives
						///////////////////////////////////////////////////////////////////
						webSocket.onmessage = function(message){
							var msg = JSON.parse(message.data);
							if(msg.runningAgents != undefined){
								$scope.runningAgents = msg.runningAgents;
								$scope.$apply();
							}
							else if(msg.agentTypes != undefined){
								$scope.agentTypes = msg.agentTypes;
								$scope.$apply();
							}
							else if(msg.performatives != undefined){
								$scope.performatives = msg.performatives;
								$scope.$apply();
							}
						}
							
						setInterval($scope.getRunningAgents, 2000);
						setInterval($scope.getAgentTypes,2000)
						setInterval($scope.getConsoleMessages, 2000);
						
		}
		])
		
		
		.controller('AgentNameController', ['$scope', 'agent', '$uibModalInstance', '$http',
		                      function($scope, agent, $uibModalInstance, $http){
									$scope.agent = agent;
									$scope.agentName = "";
									$scope.create = function(){
										$http.put("http://" + ip + ":8080/AgentsWeb/rest/agents/running/PingPong$" + $scope.agent.name + "/" + $scope.agentName);
										$uibModalInstance.close();
									}
									$scope.close = function(){
										$uibModalInstance.close();
									}
					
							}                 
		 ])
		 
		 .controller('AgentNameWSController', ['$scope', 'agent', '$uibModalInstance', '$http',
		                    function($scope, agent, $uibModalInstance, $http){
									$scope.agent = agent;
									$scope.agentName = "";
									$scope.create = function(){
										var data = {"type":"ADD_AGENT",
													"data": {"1":$scope.agent.name,
															 "2":$scope.agentName}
												    }
										webSocket.send(JSON.stringify(data));
										$uibModalInstance.close();
									}
									$scope.close = function(){
										$uibModalInstance.close();
									}
					
							}                 
		 ]);
