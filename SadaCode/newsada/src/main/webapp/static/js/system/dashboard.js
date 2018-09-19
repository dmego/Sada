var echartsA = echarts.init(document.getElementById('echartA'));
optionA = {
    tooltip: {
        trigger: 'axis',
    },
    legend: {
        data: ['图片', '文档', '视频']
    },
    grid: {
        left: '3%',
        right: '4%',
        bottom: '3%',
        containLabel: true
    },
    xAxis: [{
        type: 'category',
        boundaryGap: true,
        data: ['周一', '周二', '周三', '周四', '周五', '周六', '周日']
    }],

    yAxis: [{
        type: 'value'
    }],
    series: [{
            name: '图片',
            type: 'line',
            stack: '总数',
            areaStyle: {
                normal: {}
            },
            data: [12, 13, 15, 14, 9, 23, 10],
            itemStyle: {
                normal: {
                    color: '#59aea2'
                },
                emphasis: {

                }
            }
        },
        {
            name: '文档',
            type: 'line',
            stack: '总数',
            areaStyle: {
                normal: {}
            },
            data: [10, 12, 11, 7, 14, 15, 19],
            itemStyle: {
                normal: {
                    color: '#e7505a'
                }
            }
        },
        {
            name: '视频',
            type: 'line',
            stack: '总数',
            areaStyle: {
                normal: {}
            },
            data: [15, 12, 11, 14, 19, 10, 17],
            itemStyle: {
                normal: {
                    color: '#32c5d2'
                }
            }
        }
    ]
};
echartsA.setOption(optionA);

