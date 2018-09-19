var dom = document.getElementById("container");
var myChart = echarts.init(dom);
var app = {};
option = null;
option = {
    tooltip: {},
    animationDurationUpdate: 1500,
    animationEasingUpdate: 'quinticInOut',
    series: [{
        type: 'graph',
        layout: 'none',
        symbolSize: 50,
        roam: true,
        color: "#68e",

        label: {
            normal: {
                show: true
            }
        },
        edgeSymbol: ['circle'],
        edgeSymbolSize: [4, 10],
        edgeLabel: {
            normal: {
                textStyle: {
                    fontSize: 5
                }
            }
        },
        data: [{
            name: 'peer0.org1.example.com',
            x: 300,
            y: 350,
            label: {
                color: "#000",
                position: "left"
            },
            symbolSize: "45",
            itemStyle: {

            }
        }, {
            name: 'peer1.org1.example.com',
            x: 900,
            y: 400,
            label: {
                color: "#000",
                position: "right"
            },
            symbolSize: "45",
            itemStyle: {

            }
        }, {
            name: 'peer0.org2.example.com',
            x: 600,
            y: 600,
            label: {
                color: "#000",
                position: "right"
            },
            symbolSize: "45",
            itemStyle: {

            }
        }, {
            name: 'peer1.org2.example.com',
            x: 600,
            y: 150,
            label: {
                color: "#000",
                position: "right"
            },
            symbolSize: "45",
            itemStyle: {

            }
        }, {
            name: 'couchdb0.org1.example.com',
            x: 150,
            y: 450,
            label: {
                color: "#000",
                position: "bottom"
            },
            symbolSize: "30",
            itemStyle: {
                color: "#462"
            }
        }, {
            name: 'couchdb1.org1.example.com',
            x: 940,
            y: 240,
            label: {
                color: "#000",
                position: "right"
            },
            symbolSize: "30",
            itemStyle: {
                color: "#462"
            }
        }, {
            name: 'couchdb0.org2.example.com',
            x: 600,
            y: 750,
            label: {
                color: "#000",
                position: "right"
            },
            symbolSize: "30",
            itemStyle: {
                color: "#462"
            }
        }, {
            name: 'couchdb1.org2.example.com',
            x: 600,
            y: 0,
            label: {
                color: "#000",
                position: "right"
            },
            symbolSize: "30",
            itemStyle: {
                color: "#462"
            }
        }, {
            name: 'orderer.example.com',
            x: 600,
            y: 350,
            label: {
                color: "#000",
                position: "top"
            },
            symbolSize: "50",
            itemStyle: {
                color: "#6cf"
            }
        }, {
            name: 'ca0.org1.example.com',
            x: 300,
            y: 200,
            label: {
                color: "#000",
                position: "left"
            },
            symbolSize: "40",
            itemStyle: {
                color: "#68a"
            }
        }, {
            name: 'ca0.org2.example.com',
            x: 450,
            y: 700,
            label: {
                color: "#000",
                position: "left"
            },
            symbolSize: "40",
            itemStyle: {
                color: "#68a"
            }
        }],
        // links: [],
        links: [
            //peer0.org1    
            {
                source: 'peer0.org1.example.com',
                target: 'peer0.org2.example.com',
                lineStyle: {
                    normal: {
                        curveness: 0.2
                    }
                }
            }, {
                source: 'peer0.org1.example.com',
                target: 'peer1.org2.example.com',
                lineStyle: {
                    normal: {
                        curveness: 0.2
                    }
                }
            }, {
                source: 'peer0.org1.example.com',
                target: 'couchdb0.org1.example.com',
                lineStyle: {
                    normal: {
                        curveness: 0.2
                    }
                }
            }, {
                source: 'peer0.org1.example.com',
                target: 'ca0.org1.example.com',
                lineStyle: {
                    normal: {
                        curveness: 0.2
                    }
                }
            },
            {
                source: 'peer0.org1.example.com',
                target: 'orderer.example.com',
                lineStyle: {
                    normal: {
                        curveness: 0.2
                    }
                }
            },

            //peer1.org1    
            {
                source: 'peer1.org1.example.com',
                target: 'peer0.org2.example.com',
                lineStyle: {
                    normal: {
                        curveness: 0.2
                    }
                }
            }, {
                source: 'peer1.org1.example.com',
                target: 'peer1.org2.example.com',
                lineStyle: {
                    normal: {
                        curveness: 0.2
                    }
                }
            }, {
                source: 'peer1.org1.example.com',
                target: 'couchdb1.org1.example.com',
                lineStyle: {
                    normal: {
                        curveness: 0.2
                    }
                }
            },
            {
                source: 'peer1.org1.example.com',
                target: 'orderer.example.com',
                lineStyle: {
                    normal: {
                        curveness: 0.2
                    }
                }
            },

            //peer0.org2    
            {
                source: 'peer0.org2.example.com',
                target: 'couchdb0.org2.example.com',
                lineStyle: {
                    normal: {
                        curveness: 0.2
                    }
                }
            },
            {
                source: 'peer0.org2.example.com',
                target: 'orderer.example.com',
                lineStyle: {
                    normal: {
                        curveness: 0.2
                    }
                }
            },
            {
                source: 'peer0.org2.example.com',
                target: 'ca0.org2.example.com',
                lineStyle: {
                    normal: {
                        curveness: 0.2
                    }
                }
            },

            //peer1.org2    
            {
                source: 'peer1.org2.example.com',
                target: 'couchdb1.org2.example.com',
                lineStyle: {
                    normal: {
                        curveness: 0.2
                    }
                }
            }, {
                source: 'peer1.org2.example.com',
                target: 'orderer.example.com',
                lineStyle: {
                    normal: {
                        curveness: 0.2
                    }
                }
            }
        ],
        lineStyle: {
            normal: {
                opacity: 0.9,
                width: 2,
                curveness: 0
            }
        }
    }]
};;
if (option && typeof option === "object") {
    myChart.setOption(option, true);
}