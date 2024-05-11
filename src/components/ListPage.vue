<template>
  <div class="container mx-auto px-4 py-8">
    <h1 class="text-3xl font-bold mb-8 text-center text-blue-600">View report history</h1>
    <div class="bg-white shadow-lg rounded-lg overflow-hidden">
      <div class="row bg-blue-500 text-white uppercase tracking-wider text-sm font-semibold border-b-4 border-blue-600">
        <div class="col p-4" style="border-bottom: 1px solid #333; font-size: 25px; font-weight: 200; text-align: center;">Date</div>
        <div class="col p-4" style="border-bottom: 1px solid #333; font-size: 25px; font-weight: 200; text-align: center;">Progress status</div>
      </div>
      <div v-for="report in paginatedReports" :key="report.date" style="margin-top: 25px; border-bottom: 1px solid #333; text-align: center;">
        <div class="row border-b-2 border-gray-200 hover:bg-blue-50 transition duration-300 ease-in-out">
          <div class="col p-4">{{ report.date }}</div>
          <div class="col p-4">{{ report.status }}</div>
        </div>
      </div>
    </div>
    <div class="flex justify-center space-x-2 mt-8" style="margin-top: 30px; text-align: center;">
      <button v-for="page in totalPages" :key="page" @click="currentPage = page" class="px-4 py-2 rounded-full text-sm font-medium transition duration-300 ease-in-out" :class="{ 'bg-blue-500 text-white': currentPage === page, 'text-gray-500 hover:text-blue-600': currentPage !== page }">
        {{ page }}
      </button>
      <div class="flex justify-center mt-8" style="margin-top: 10px;"> <button @click="deleteAllReports" class="px-6 py-3 bg-red-500 text-white rounded-full shadow-md hover:bg-red-600 transition duration-300 ease-in-out" > 모든 데이터 삭제 </button> </div>
    </div>
  </div>
</template>

<script>
export default {
  data() {
    return {
      reports: [],
      currentPage: 1,
      perPage: 5
    };
  },
  computed: {
    totalPages() {
      return Math.ceil(this.reports.length / this.perPage);
    },
    paginatedReports() {
      const startIndex = (this.currentPage - 1) * this.perPage;
      const endIndex = startIndex + this.perPage;
      return this.reports.slice(startIndex, endIndex);
    }
  },
  created() {
    const storedReports = JSON.parse(localStorage.getItem('reports')) || [];
    this.reports = storedReports;
  },
  methods: {
    deleteAllReports() {
      this.reports = [];
      localStorage.removeItem('reports');
    }
  }
};
</script>

<style scoped>
.container {
  max-width: 100%;
  padding: 0 15px;
}

.row {
  display: flex;
  flex-wrap: wrap;
}

.col {
  flex: 1;
  padding: 10px;
  text-align: center;
}

@media (min-width: 768px) {
  .container {
    max-width: 800px;
    padding: 0;
  }
}
</style>