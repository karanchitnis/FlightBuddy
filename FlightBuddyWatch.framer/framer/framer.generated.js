// This is autogenerated by Framer Studio


// Generated by CoffeeScript 1.8.0
(function() {
  var lookupLine, properties, _RESULT,
    __slice = [].slice;

  if (window.FramerStudio == null) {
    window.FramerStudio = {};
  }

  window.onerror = null;

  window.midiCommand = window.midiCommand || function() {};

  if (Framer.Device) {
    properties = ["deviceScale", "contentScale", "deviceType", "keyboard", "orientation", "fullScreen"];
    properties.map(function(propertyName) {
      return Framer.Device.on("change:" + propertyName, function() {
        return window._bridge("device:change");
      });
    });
  }

  _RESULT = null;

  lookupLine = function(lineNumber) {
    var char, charIndex, errorColNumber, errorLine, errorLineIndex, errorLineNumber, loc, sourceLines, _i, _len;
    sourceLines = _RESULT.js.split("\n");
    errorLineIndex = lineNumber - 1;
    errorLine = sourceLines[errorLineIndex];
    if (!errorLine) {
      return lineNumber;
    }
    errorLineNumber = 1;
    errorColNumber = 0;
    for (charIndex = _i = 0, _len = errorLine.length; _i < _len; charIndex = ++_i) {
      char = errorLine[charIndex];
      loc = _RESULT.sourceMap.sourceLocation([errorLineIndex, charIndex]);
      if (loc && loc[0] > errorLineNumber) {
        errorLineNumber = loc[0] + 1;
        errorColNumber = loc[1];
      }
    }
    console.log("lineNumber", lineNumber);
    console.log("errorLineNumber", errorLineNumber);
    return errorLineNumber;
  };

  FramerStudio.compile = function(code) {
    var e, err, errorMessage;
    console.log("FramerStudio.compile");
    window.onerror = null;
    window.onresize = null;
    try {
      _RESULT = CoffeeScript.compile(code, {
        sourceMap: true,
        filename: "generated.js"
      });
    } catch (_error) {
      e = _error;
      console.log("Compile error:", e);
      if (e instanceof SyntaxError) {
        errorMessage = e.stack;
        err = new SyntaxError(e.message);
        err.line = e.location.first_line;
        err.lineNumber = e.location.first_line;
        err.lookup = true;
        window._bridge("StudioError", {
          message: e.message,
          line: e.location.first_line,
          lineNumber: e.location.first_line,
          errorType: "compile"
        });
        throw err;
      } else {
        throw e;
      }
    }
    window.onerror = function(errorMsg, url, lineNumber) {
      var error;
      console.log.apply(console, ["Eval error:"].concat(__slice.call(arguments)));
      error = new Error(errorMsg);
      error.line = lookupLine(lineNumber);
      window._bridge("StudioError", {
        message: errorMsg,
        line: error.line,
        lineNumber: error.line,
        errorType: "eval"
      });
      throw error;
    };
    return _RESULT.js;
  };

  if (typeof window._bridge === "function") {
    window._bridge("StudioScriptLoaded");
  }

}).call(this);

window.__imported__ = window.__imported__ || {};
window.__imported__["FlightBuddyWatch/layers.json.js"] = [
	{
		"id": 39,
		"name": "FlightBuddyWatch16",
		"layerFrame": {
			"x": 0,
			"y": 0,
			"width": 333,
			"height": 379
		},
		"maskFrame": null,
		"image": {
			"path": "images/FlightBuddyWatch16.png",
			"frame": {
				"x": 0,
				"y": 0,
				"width": 333,
				"height": 379
			}
		},
		"imageType": "png",
		"children": [
			
		],
		"modification": "475821111"
	},
	{
		"id": 33,
		"name": "FlightBuddyWatch15",
		"layerFrame": {
			"x": 0,
			"y": 0,
			"width": 333,
			"height": 379
		},
		"maskFrame": null,
		"image": {
			"path": "images/FlightBuddyWatch15.png",
			"frame": {
				"x": 0,
				"y": 0,
				"width": 333,
				"height": 379
			}
		},
		"imageType": "png",
		"children": [
			
		],
		"modification": "1491187729"
	},
	{
		"id": 27,
		"name": "FlightBuddyWatch14",
		"layerFrame": {
			"x": 0,
			"y": 0,
			"width": 333,
			"height": 379
		},
		"maskFrame": null,
		"image": {
			"path": "images/FlightBuddyWatch14.png",
			"frame": {
				"x": 0,
				"y": 0,
				"width": 333,
				"height": 379
			}
		},
		"imageType": "png",
		"children": [
			
		],
		"modification": "41811413"
	},
	{
		"id": 21,
		"name": "FlightBuddyWatch13",
		"layerFrame": {
			"x": 0,
			"y": 0,
			"width": 333,
			"height": 379
		},
		"maskFrame": null,
		"image": {
			"path": "images/FlightBuddyWatch13.png",
			"frame": {
				"x": 0,
				"y": 0,
				"width": 333,
				"height": 379
			}
		},
		"imageType": "png",
		"children": [
			
		],
		"modification": "138321584"
	},
	{
		"id": 14,
		"name": "FlightBuddyWatch12",
		"layerFrame": {
			"x": 0,
			"y": 0,
			"width": 333,
			"height": 379
		},
		"maskFrame": null,
		"image": {
			"path": "images/FlightBuddyWatch12.png",
			"frame": {
				"x": 0,
				"y": 0,
				"width": 333,
				"height": 379
			}
		},
		"imageType": "png",
		"children": [
			
		],
		"modification": "952524025"
	},
	{
		"id": 159,
		"name": "FlightBuddyWatch11",
		"layerFrame": {
			"x": 0,
			"y": 0,
			"width": 333,
			"height": 379
		},
		"maskFrame": null,
		"image": {
			"path": "images/FlightBuddyWatch11.png",
			"frame": {
				"x": 0,
				"y": 0,
				"width": 333,
				"height": 379
			}
		},
		"imageType": "png",
		"children": [
			
		],
		"modification": "1066519673"
	},
	{
		"id": 168,
		"name": "FlightBuddyWatch10",
		"layerFrame": {
			"x": 0,
			"y": 0,
			"width": 333,
			"height": 379
		},
		"maskFrame": null,
		"image": {
			"path": "images/FlightBuddyWatch10.png",
			"frame": {
				"x": 0,
				"y": 0,
				"width": 333,
				"height": 379
			}
		},
		"imageType": "png",
		"children": [
			
		],
		"modification": "2004617715"
	},
	{
		"id": 110,
		"name": "FlightBuddyWatch9",
		"layerFrame": {
			"x": 0,
			"y": 0,
			"width": 333,
			"height": 379
		},
		"maskFrame": null,
		"image": {
			"path": "images/FlightBuddyWatch9.png",
			"frame": {
				"x": 0,
				"y": 2,
				"width": 333,
				"height": 377
			}
		},
		"imageType": "png",
		"children": [
			
		],
		"modification": "818106322"
	},
	{
		"id": 147,
		"name": "FlightBuddyWatch8",
		"layerFrame": {
			"x": 0,
			"y": 0,
			"width": 333,
			"height": 379
		},
		"maskFrame": null,
		"image": {
			"path": "images/FlightBuddyWatch8.png",
			"frame": {
				"x": 0,
				"y": 0,
				"width": 333,
				"height": 379
			}
		},
		"imageType": "png",
		"children": [
			
		],
		"modification": "1285184654"
	},
	{
		"id": 54,
		"name": "FlightBuddyWatch7",
		"layerFrame": {
			"x": 0,
			"y": 0,
			"width": 333,
			"height": 379
		},
		"maskFrame": null,
		"image": {
			"path": "images/FlightBuddyWatch7.png",
			"frame": {
				"x": 0,
				"y": 0,
				"width": 333,
				"height": 379
			}
		},
		"imageType": "png",
		"children": [
			
		],
		"modification": "1070590980"
	},
	{
		"id": 48,
		"name": "FlightBuddyWatch6",
		"layerFrame": {
			"x": 0,
			"y": 0,
			"width": 333,
			"height": 379
		},
		"maskFrame": null,
		"image": {
			"path": "images/FlightBuddyWatch6.png",
			"frame": {
				"x": 0,
				"y": 0,
				"width": 333,
				"height": 379
			}
		},
		"imageType": "png",
		"children": [
			
		],
		"modification": "1013353977"
	},
	{
		"id": 138,
		"name": "FlightBuddyWatch5",
		"layerFrame": {
			"x": 0,
			"y": 0,
			"width": 333,
			"height": 379
		},
		"maskFrame": null,
		"image": {
			"path": "images/FlightBuddyWatch5.png",
			"frame": {
				"x": 0,
				"y": 0,
				"width": 333,
				"height": 379
			}
		},
		"imageType": "png",
		"children": [
			
		],
		"modification": "892501450"
	},
	{
		"id": 121,
		"name": "FlightBuddyWatch4",
		"layerFrame": {
			"x": 0,
			"y": 0,
			"width": 333,
			"height": 379
		},
		"maskFrame": null,
		"image": {
			"path": "images/FlightBuddyWatch4.png",
			"frame": {
				"x": 0,
				"y": 0,
				"width": 333,
				"height": 379
			}
		},
		"imageType": "png",
		"children": [
			
		],
		"modification": "285843288"
	},
	{
		"id": 126,
		"name": "FlightBuddyWatch3",
		"layerFrame": {
			"x": 0,
			"y": 0,
			"width": 333,
			"height": 379
		},
		"maskFrame": null,
		"image": {
			"path": "images/FlightBuddyWatch3.png",
			"frame": {
				"x": 0,
				"y": 0,
				"width": 333,
				"height": 379
			}
		},
		"imageType": "png",
		"children": [
			
		],
		"modification": "1635860753"
	},
	{
		"id": 116,
		"name": "FlightBuddyWatch2",
		"layerFrame": {
			"x": 0,
			"y": 0,
			"width": 333,
			"height": 379
		},
		"maskFrame": null,
		"image": {
			"path": "images/FlightBuddyWatch2.png",
			"frame": {
				"x": 0,
				"y": 0,
				"width": 333,
				"height": 379
			}
		},
		"imageType": "png",
		"children": [
			
		],
		"modification": "1401200660"
	},
	{
		"id": 104,
		"name": "FlightBuddyWatch1",
		"layerFrame": {
			"x": 0,
			"y": 0,
			"width": 333,
			"height": 379
		},
		"maskFrame": null,
		"image": {
			"path": "images/FlightBuddyWatch1.png",
			"frame": {
				"x": 0,
				"y": 0,
				"width": 333,
				"height": 379
			}
		},
		"imageType": "png",
		"children": [
			
		],
		"modification": "1189636989"
	}
]
window.Framer.Defaults.DeviceView = {
  "deviceScale" : -1,
  "orientation" : 0,
  "contentScale" : 1,
  "deviceType" : "apple-watch"
};

window.FramerStudioInfo = {
  "deviceImagesUrl" : "file:\/\/\/Users\/briancly\/Downloads\/Framer%20Studio.app\/Contents\/Resources\/DeviceImages\/"
};

Framer.Device = new Framer.DeviceView();
Framer.Device.setupContext();