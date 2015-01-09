angular.module('Bubble').factory('bubbleFactory', function (utils, jsonValue, $location, termService, $translate) {
  var terms = [];
  var scope;

  var $$ = {
    getBox: function (width, height) {
      var wBox = Math.min(width, height);
      var box = {
        width: wBox,
        pi2: Math.PI * 2,
        xCenter: wBox / 2,
        yCenter: wBox / 2,
        radiusInner: wBox / 3.5,
        radiusOuter: wBox / 2,
        radiusMin: 55, //it is in pixel
        fontMin: 14, //it is in pixel
        fontMax: 20, //it is in pixel,
        delta: 20
      }
      box.radiusMax = (box.radiusOuter - box.radiusInner) / 2 + box.delta;
      if (utils.isMobile()) {box.radiusMin = 50;}
      return box;
    },

    // TODO might be a infinity loop
    getCircles: function (box) {
      var randomCircles = [];
      var termCountMax = terms.maxBy("count");
      //var angle = -1;
      var times = 0;
      while (randomCircles.length < terms.length && ++times < 5000) {
        var value = terms[randomCircles.length].count;
        var radius = Math.max((value * box.radiusMax) / termCountMax, box.radiusMin);
        var distFromCenter = box.radiusInner + radius + Math.random() * (box.radiusOuter - box.radiusInner - radius * 2);
        var angle = Math.random() * box.pi2;
        var cx = box.xCenter + distFromCenter * Math.cos(angle);
        var cy = box.yCenter + distFromCenter * Math.sin(angle);

        var hit = false;
        $.each(randomCircles, function (i, circle) {
          var dx = circle.cx - cx;
          var dy = circle.cy - cy;
          var r = circle.radius + radius;
          if (dx * dx + dy * dy < Math.pow(r - box.delta, 2)) {
            hit = true;
            return false;
          }
        });
        if (!hit) {
          $$.acceptCircle(cx, cy, radius, value, box, termCountMax, randomCircles);
        }
      }
      if (randomCircles.length < terms.length) {
        ++box.delta;
        return $$.getCircles(box);
      }
      return randomCircles;
    },

    acceptCircle: function (cx, cy, radius, value, box, termCountMax, randomCircles) {
      var fontSize = Math.max((value * box.fontMax) / termCountMax, box.fontMin);
      randomCircles.push({
        cx: cx - radius < 0 ? radius : (cx + radius) > box.width ? box.width - radius : cx,
        cy: cy - radius < 0 ? radius : (cy + radius) > box.width ? box.width - radius : cy,
        radius: radius,
        data: terms[randomCircles.length],
        color: terms[randomCircles.length].color,
        termLabel: {
          fontSize: fontSize,
          dy: radius / 3,
          label: terms[randomCircles.length].label
        },
        termCount: {
          fontSize: Math.min(fontSize * 1.5, box.fontMax)
        }
      })
    },

    renderCircle: function (node) {
      node.append("circle")
        .attr("r", function (d) {return d.radius;})
        .attr("cx", function (d) {return d.cx;})
        .attr("cy", function (d) {return d.cy;})
        .style("fill", function (d) { return d.color; })
        .attr("opacity", "0.8");
    },

    renderTermCount: function (node) {
      node.append("text")
        .classed({termCount: true})
        .attr("dy", "-5px")
        .style("font-size", function (d) { return d.termCount.fontSize + "px";})
        .style("font-weight", "300")
        .text(function (d) { return d.data.count; });
    },

    calculateTextSize: function (d, i) {
      var width = this.getBBox().width;
      while ((width = this.getBBox().width + 5) > 2 * d.radius) {
        d3.select(this).style("font-size", function (d) {return --d.termLabel.fontSize + "px";});
      }
    },

    calculateTextSizeInBigBox: function (d, i) {
      var fontSize = d.termLabel.fontSize;
      var width = this.getBBox().width;
      while ((width = this.getBBox().width + 5) <= 2 * d.radius) {
        d3.select(this).style("font-size", function (d) {return --fontSize + "px";});
      }
    },

    renderTermLabel: function (node, box) {
      node.append("text")
        .classed({termLabel: true})
        //.each(function(d, i) {
        //  var text = d3.select(this);
        //  var labels = d.termLabel.labels;
        //  var fontSize = d.termLabel.fontSize + 1 - labels.length * 5;
        //  $.each(d.termLabel.labels, function(j, label) {
        //    text.append("tspan")
        //      .attr("dy", function(d){return d.termLabel.dy + fontSize * j + "px";})
        //      .attr("x", function (d) {return d.cx;})
        //      .attr("y", function (d) {return d.cy;})
        //      .text(function (d) {return label;});
        //  });
        //  d.termLabel.fontSize = Math.max(fontSize, box.fontMin);
        //})
        .text(function (d) {return d.termLabel.label;})
        .attr("dy", function (d) {return d.termLabel.dy + "px";})
        .attr("x", function (d) {return d.cx;})
        .attr("y", function (d) {return d.cy;})
        .style("font-size", function (d) { return d.termLabel.fontSize + "px"; })
        .each($$.calculateTextSize)
        .style("font-size", function (d) { return d.termLabel.fontSize + "px"; });
    },

    setStyleText: function (node) {
      node.selectAll("text")
        .attr("x", function (d) {return d.cx;})
        .attr("y", function (d) {return d.cy;})
        .style("font-family", "roboto,arial")
        .style("text-anchor", "middle")
        .style("fill", "white");
    },

    moveToCenter: function (node, box) {
      var toCenterPoint = d3.svg.transform()
        .translate(function (d) {
          var cx = d3.select(this).select('circle').attr("cx");
          var dx = box.xCenter - d.cx;
          var dy = box.yCenter - d.cy;
          return [dx, dy];
        });

      node.classed({node: true, active: true})
        .transition().duration(1000)
        .attr('transform', toCenterPoint)
        .select("circle")
        .attr('r', function (d) {
          return box.radiusInner;
        })
        .each("end", function () {
          node.append("text").classed({clickMe: true})
            .attr("dy", function (d) {return "60px";})
            .style({
              "font-size": "12px",
              "font-style": "italic",
              "font-family": "roboto,arial",
              "text-anchor": "middle",
              "fill": "white"
            })
            .attr("x", function (d) {return node.select("text.termLabel").attr("x");})
            .attr("y", function (d) {return node.select("text.termLabel").attr("y");});
          $translate("clickBubbleChartTip").then(function (translation) {
            node.select(".clickMe").text(translation).style("opacity", 0).transition().duration(500).style("opacity", "0.8");
          });
        });

      node.selectAll("text.termCount").transition().duration(1000)
        .style("font-size", function (d) { return utils.isMobile() ? "35px" : "45px";});

      node.selectAll("text.termLabel").transition().duration(1000)
        .attr("dy", function (d) {return "40px";})
        .style("font-size", function (d) { return utils.isMobile() ? "25px" : "30px";});

    },

    moveToReflection: function (node, box, swapped) {
      var toReflectionPoint = d3.svg.transform()
        .translate(function (d) {
          var dx = 2 * (box.xCenter - d.cx);
          var dy = 2 * (box.yCenter - d.cy);
          return [dx, dy];
        });

      node.transition()
        .duration(1000)
        .delay(function (d, i) {return i * 10;})
        .attr('transform', swapped ? "" : toReflectionPoint)
        .select("circle")
        .attr('r', function (d) {
          return d.radius;
        });
    },

    reset: function (node) {
      node.classed({active: false});
      node.selectAll("text.clickMe").remove();

      node.selectAll("text.termCount").transition().duration(1000)
        .style("font-size", function (d) { return d.termCount.fontSize + "px";});

      node.selectAll("text.termLabel").transition().duration(1000)
        .attr("dy", function (d) {return d.termLabel.dy;})
        .style("font-size", function (d) { return d.termLabel.fontSize + "px";});
    },

    registerClickEvent: function (box, svg) {
      var swapped = false;
      var node = svg.selectAll(".node");
      node.style("cursor", "pointer").on("click", function (d) {
        var d3Node = d3.select(this);
        if (d3Node.selectAll("text.clickMe")[0].length === 1) {
          utils.go2SkillAnalyticPage(scope, d3Node.attr("class").split(" ")[1]);
          return false;
        }
        $$.reset(node);
        $$.moveToCenter(d3Node, box);
        $$.moveToReflection(svg.selectAll(".node:not(.active)"), box, swapped);
        swapped = !swapped;
      });
    },

    selectBiggest: function (box) {
      var max = terms.maxBy("count");
      var termName = 0;
      $.each(terms, function (i, term) {
        if (max === term.count) {
          termName = term.term;
          return false;
        }
      });
      $$.moveToCenter(d3.select([".node", ".", termName].join("")), box);
    },

    enableNotifications: function () {
      return utils.getView() === jsonValue.views.bubbleChart;
    },

    changeLang: function () {
      $translate("clickBubbleChartTip").then(function (translation) {
        d3.select(".clickMe").text(translation);
      });
    },
    registerResponsive: function (svg) {
      $(window).resize(function () {
        var width = $(".bubble-chart-container").width();
        svg.attr("width", width);
        svg.attr("height", width);
      });
      $(window).resize();
    },
    unregisterResponsive: function () {
      $(window).off("resize");
    }
  }

  utils.registerNotification(jsonValue.notifications.switchScope, function ($scope) {scope = $scope}, $$.enableNotifications);
  utils.registerNotification(jsonValue.notifications.changeLang, $$.changeLang);
  utils.registerNotification(jsonValue.notifications.changeUrl, function () {$$.unregisterResponsive();}, $$.enableNotifications);

  var instance = {

    renderView: function ($terms) {
      terms = termService.toViewTerms($terms).shuffle();
      //terms = terms.slice(3);

      //var size = utils.isMobile() ? 300 : 500;//  $(".bubble-chart-container").width();
      var size = 600;//  $(".bubble-chart-container").width();
      var box = $$.getBox(size, size);
      var randomCircles = $$.getCircles(box);

      $(".bubble-chart-container").find("svg.bubbleChart").remove(); //remove all bubble chart svg
      var svg = d3.select(".bubble-chart-container").append("svg")
        .attr({preserveAspectRatio: "xMidYMid", width: size, height: size, class: "bubbleChart"})
        .attr("viewBox", function (d) {return ["0 0", size, size].join(" ")});
      //.style("background", "white");

      var node = svg.selectAll(".node")
        .data(randomCircles)
        .enter().append("g")
        .attr("class", function (d) {return ["node", d.data.term].join(" ");});

      $$.renderCircle(node);
      $$.renderTermCount(node);
      $$.renderTermLabel(node, box);
      $$.setStyleText(node);
      $$.registerClickEvent(box, svg);
      $$.selectBiggest(box);
      $$.registerResponsive(svg);

      node.sort(function (a, b) {return b.data.count - a.data.count;});
    },

    updateViewTerm: function (term) {
      var node = d3.select(["g.node", term.term].join("."));
      var nodeTermCount = node.select("text.termCount");

      if (nodeTermCount.text() === "" + term.count) {
        return false;
      }
      var circleTerm = node.select("circle");
      circleTerm.style({"stroke": '#fff', 'stroke-width': 3}).transition().duration(1000).style("stroke-width", 0);

      nodeTermCount.text(term.count).style("opacity", 0).transition().duration(1000).style("opacity", "1");
    }
  }

  return instance;
});