akka-cluster-service-locator
============================

A very simple and quick prototype to model distrinuted services fremawork.
Frmework features:
  - service registry: watches the cluster for new nodes, and discovers available services
  - service provider(s): when added to cluster dynamically register their services with locator
  - service client(s): locate services they need usind service locator, then send requests directly to located services
  
  
