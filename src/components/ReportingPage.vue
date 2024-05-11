<template>
  <div class="container">
    <h1 class="text-xl font-bold text-center mb-4">Reporting!</h1>
    <form @submit.prevent="handleSubmit">
      <div class="input-field">
        <label class="input-label">Image</label>
        <input type="file" id="file" class="input" accept="image/*" ref="fileInput" @change="previewImage">
        <small>Please upload the image</small>
        <div class="preview-image">
          <img v-if="imageUrl" :src="imageUrl" alt="Preview Image">
          <p v-else>No Image</p>
        </div>
      </div>
      <div class="input-field">
        <label class="input-label">Sighting time</label>
        <input type="text" id="date" class="input" placeholder="ex)2024-05-16" v-model="date">
      </div>
      <div class="input-field">
        <label class="input-label">Sighting location</label>
        <input type="text" id="location" class="input" placeholder="Please enter a location" v-model="location">
      </div>
      <div class="input-field">
        <label class="input-label">Additional content</label>
        <textarea id="content" class="textarea" rows="4" placeholder="If you have anything to add, please write it down" v-model="content" style="font-size: 20px;" spellcheck="false"></textarea>
      </div>
      <div class="flex justify-between">
        <button type="button" class="button cancel-button">Cancel</button>
        <button type="submit" class="button submit-button" style="text-align: right;">Report!</button>
      </div>
    </form>
  </div>
</template>

<script>
export default {
  data() {
    return {
      date: '',
      location: '',
      content: '',
      imageUrl: ''
    }
  },
  methods: {
    handleSubmit() {
      const newReport = {
        date: this.date,
        status: 'Processing completion'
      };
      let reports = JSON.parse(localStorage.getItem('reports')) || [];
      reports.push(newReport);
      localStorage.setItem('reports', JSON.stringify(reports));
    },
    previewImage(event) {
      const file = event.target.files[0];
      this.imageUrl = URL.createObjectURL(file);
    }
  }
}
</script>

<style>
.container {
  max-width: 100%;
  margin: 50px auto;
  padding: 20px;
  background-color: #ffffff;
  border-radius: 8px;
  box-shadow: 0 4px 6px rgba(0, 0, 0, 0.1);
}

.input-field {
  margin-bottom: 20px;
}

.input-label {
  display: block;
  margin-bottom: 5px;
  font-weight: bold;
}

.input,
.textarea {
  width: 100%;
  padding: 8px;
  border: 1px solid #d1d5db;
  border-radius: 4px;
}

.button {
  padding: 10px 20px;
  border: none;
  border-radius: 4px;
  color: #ffffff;
  cursor: pointer;
}

.submit-button {
  background-color: #4caf50;
}

.cancel-button {
  background-color: #f44336;
}

.preview-image {
  margin-top: 10px;
  text-align: center;
}

.preview-image img {
  max-width: 100%;
  height: auto;
}

.preview-image p {
  margin: 0;
  color: #999;
}

@media (min-width: 768px) {
  .container {
    max-width: 600px;
  }
}
</style>