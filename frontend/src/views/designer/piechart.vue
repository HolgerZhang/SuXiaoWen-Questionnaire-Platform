<template>
  <div :id="'mychart'+index" :style="myChartStyle"></div>
</template>

<script>
import * as echarts from "echarts";

export default {
  name: 'piechart',
  props: ['index', 'qs'],
  setup() {
    let Data = [{
      name: 'A',
      value: 20,
    },
      {
        name: 'B',
        value: 10,
      },
      {
        name: 'C',
        value: 30,
      },
      {
        name: 'D',
        value: 40,
      },
    ];
    let myChartStyle = {float: "left", width: "100%", height: "270px"}
    return {
      Data,
      myChartStyle,
    };
  },
  mounted() {
    this.init(this.qs)
  },
  methods: {
    init(qs) {
      this.Data = []
      for (let i in qs.information) {
        this.Data.push({
          name: qs.information[i].title,
          value: qs.information[i].num
        })
      }
      this.initEcharts();
    },
    initEcharts() {
      const option = {
        series: [
          {
            type: "pie",
            data: this.Data
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
