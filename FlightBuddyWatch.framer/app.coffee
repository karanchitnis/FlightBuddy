# This imports all the layers for "FlightBuddyWatch" into flightbuddywatchLayers
flightbuddywatchLayers = Framer.Importer.load "imported/FlightBuddyWatch"

for layerGroupName of flightbuddywatchLayers
	window[layerGroupName] = flightbuddywatchLayers[layerGroupName]
	
FlightBuddyWatch1.x = -6
FlightBuddyWatch2.x = -6
FlightBuddyWatch3.x = -6
FlightBuddyWatch4.x = -6
FlightBuddyWatch5.x = -6
FlightBuddyWatch6.x = -6
FlightBuddyWatch7.x = -6
FlightBuddyWatch8.x = -6
FlightBuddyWatch9.x = -6
FlightBuddyWatch10.x = -6
FlightBuddyWatch11.x = -6
FlightBuddyWatch12.x = -6
FlightBuddyWatch13.x = -6
FlightBuddyWatch14.x = -6
FlightBuddyWatch15.x = -6
FlightBuddyWatch16.x = -6

FlightBuddyWatch1.on Events.Click, ->
	FlightBuddyWatch1.animate
		properties:
			x: -333
		curve: "ease-in-out"
		delay: 0
		time: 1
		
FlightBuddyWatch2.on Events.Click, ->
	FlightBuddyWatch2.opacity = 0
	FlightBuddyWatch2.animate
		properties:
			x:-333
		
FlightBuddyWatch3.on Events.Click, ->
	FlightBuddyWatch3.opacity = 0
	FlightBuddyWatch3.animate
		properties:
			x:-333
		
FlightBuddyWatch4.on Events.Click, ->
	FlightBuddyWatch4.opacity = 0
	FlightBuddyWatch4.animate
		properties:
			x:-333

FlightBuddyWatch5.on Events.Click, ->
	FlightBuddyWatch5.animate
		properties:
			x: -333
		curve: "ease-in-out"
		delay: 0
		time: 1
		
FlightBuddyWatch6.on Events.Click, ->
	FlightBuddyWatch6.animate
		properties:
			x: -333
		curve: "ease-in-out"
		delay: 0
		time: 1

FlightBuddyWatch7.on Events.Click, ->
	FlightBuddyWatch7.animate
		properties:
			x: 333
		curve: "ease-in-out"
		delay: 0
		time: 1

FlightBuddyWatch8.on Events.Click, ->
	FlightBuddyWatch8.animate
		properties:
			x: -333
		curve: "ease-in-out"
		delay: 0
		time: 1

FlightBuddyWatch9.on Events.Click, ->
	FlightBuddyWatch9.animate
		properties:
			x: 333
		curve: "ease-in-out"
		delay: 0
		time: 1

FlightBuddyWatch10.on Events.Click, ->
	FlightBuddyWatch10.animate
		properties:
			x: 333
		curve: "ease-in-out"
		delay: 0
		time: 1

FlightBuddyWatch11.on Events.Click, ->
	FlightBuddyWatch11.animate
		properties:
			x: -333
		curve: "ease-in-out"
		delay: 0
		time: 1

FlightBuddyWatch12.on Events.Click, ->
	FlightBuddyWatch12.animate
		properties:
			x: -333
		curve: "ease-in-out"
		delay: 0
		time: 1

FlightBuddyWatch13.on Events.Click, ->
	FlightBuddyWatch13.animate
		properties:
			x: -333
		curve: "ease-in-out"
		delay: 0
		time: 1

FlightBuddyWatch14.on Events.Click, ->
	FlightBuddyWatch14.animate
		properties:
			x: -333
		curve: "ease-in-out"
		delay: 0
		time: 1

FlightBuddyWatch15.on Events.Click, ->
	FlightBuddyWatch15.animate
		properties:
			x: -333
		curve: "ease-in-out"
		delay: 0
		time: 1