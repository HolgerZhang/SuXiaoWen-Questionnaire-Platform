<template>
  <div :id="'mychart'+index" :style="myChartStyle"></div>
</template>

<script>
import * as echarts from "echarts";

export default {
  name: 'myhistogram',
  props: ['index', 'qs'],
  setup() {
    let xData = ["A", "B", "C", "D"];
    let yData = [20, 10, 30, 40];
    let myChartStyle = {float: "left", width: "100%", height: "300px"}
    return {
      xData,
      yData,
      myChartStyle,
    };
  },
  mounted() {
    this.init(this.qs)
  },
  methods: {
    init(qs) {
      this.xData = []
      this.yData = []
      for (let i in qs.information) {
        this.xData.push(qs.information[i].title)
        this.yData.push(qs.information[i].num)
      }
      this.initEcharts();
    },
    initEcharts() {
      const option = {
        xAxis: {
          data: this.xData
        },
        yAxis: {},
        series: [
          {
            type: "bar",
            data: this.yData
          }
        ]
      };
      const myChart = echarts.init(document.getElementById("mychart" + this.index));
      myChart.setOption(option);
      window.addEventListener("resize", () => {
        myChart.resize();
      });
    }
  },
  watch: {
    qs(newVal) {
      this.init(newVal)
    }
  }
};
</script>

<style>

</style>
