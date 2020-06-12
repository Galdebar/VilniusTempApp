<template>
	<div id="current-temp" class="flex-wrap masking-shadow-bottom">
		<div class="no-wrap-flex">
			<div class="updated-date">
				<div class="no-wrap-flex date">
					<div class="vertical-flex-center">
						<h3>
							{{ year }}
						</h3>
						<div class="no-wrap-flex">
							<h3>{{ month }}</h3>
							<h3>{{ day }}</h3>
						</div>
					</div>

					<div class="no-wrap-flex time">
						<h3>{{ hour }}:{{ minute }}</h3>
					</div>
				</div>
			</div>

			<div class="temperature">
				<h3>{{ latestTemperature.temperature }}</h3>
			</div>
		</div>
	</div>
</template>
<script>
import { dateFromISO8601 } from "./DateFormatter";

export default {
	name: "CurrentTemp",
	data() {
		return {
			testDate: "",
			year: 2020,
			month: 6,
			day: 7,
			hour: 20,
			minute: 59
		};
	},
	computed: {
		latestTemperature: function() {
			let temperature = this.$store.getters.getLatestTemp;
			this.setDate(temperature.observationTime);
			return temperature;
		}
	},
	methods: {
		async setDate(dateTime) {
			if (dateTime !== undefined) {
				let parsedDate = dateFromISO8601(dateTime);
				this.year = parsedDate.getFullYear();
				this.month = parsedDate.getMonth();
				this.day = parsedDate.getDate();
				this.hour = parsedDate.getHours();
				this.minute = parsedDate.getMinutes();
				if(this.minute == 0){
					this.minute = "00";
				}
			}
		}
	}
};
</script>

<style lang="scss" scoped>
@import "../assets/scss/GlobalComponents";

#current-temp {
	background-color: $brand-color;
	padding: 0 24px;

	h1 {
		width: 100%;
		text-align: center;
		letter-spacing: $extra-small-distance;
	}

	.updated-date {
		h3 {
			font-size: 2em;
			color: $default-black;
		}
	}

	.time {
		padding: 0 $default-distance;

		h3 {
			font-size: 5.5em;
		}
	}

	.temperature {
		padding-left: $default-distance;

		::after {
			content: "\00b0";
		}

		h3 {
			font-size: 5.5em;
			color: $default-white;
		}
	}
}
</style>