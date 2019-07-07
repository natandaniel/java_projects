# This repository holds some of my JAVA/JEE projects with instructions to run and test them. 

# ws.soap.time 

A simple illustration of an RPC style SOAP Web Service built with JAX-WS. 

Gives the time.

The purpose is to demonstrate how easily a SOAP web service can be built when only simple data types are used.

# ws.soap.cats 

A simple illustration of a DOCUMENT style SOAP Web Service built with JAX-WS.

Describes the British Shorthair cat.

The purpose is to demonstrate how to build such a web service with custom data types using the wsgen utility and how to build client code easing the development of a client for the service with wsimport. 

I also added in the mix some SOAP API code with a SOAP handler that can monitor SOAP requests/responses. SOAP handlers are useful not only for monitoring but also for securing the acces to a service through credentials checking etc.

# ws.rest.cats

A simple illustration of a RESTful Web Service built with JAX-WS. 

Allows GETs and POSTs on the cats.xml resource.

One can GET an up to date XML representation of the cats.xml resource.

One can POST a new cat to the service and add a new object to the cats.xml resource.

