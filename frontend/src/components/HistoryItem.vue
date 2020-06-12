<template>
	<div class="separator-bottom-dark">
		<div class="no-wrap-flex">
			<div class="date">
				<p>
					{{ year }}.{{ month }}.{{ day }} <span>{{ hour }}:{{ minute }}</span>
				</p>
			</div>

			<div class="temperature">
				<p>{{ temperatureItem.temperature }}</p>
			</div>
		</div>
	</div>
</template>

<script>
import { dateFromISO8601 } from "./DateFormatter";

export default {
	name: "HistoryItem",
	props: {
		temperatureItem: Object
	},
	data() {
		return {
			year: "",
			month: "",
			day: "",
			hour: "",
			minute: ""
		};
	},
	mounted() {
		this.setDate(this.temperatureItem.observationTime);
	},
	methods: {
		setDate(dateTime) {
			let parsedDate = dateFromISO8601(dateTime);
			this.year = parsedDate.getFullYear();
			this.month = parsedDate.getMonth();
			this.day = parsedDate.getDate();
			this.hour = parsedDate.getHours();
			this.minute = parsedDate.getMinutes();
			if (this.minute == 0) {
				this.minute = "00";
			}
		}
	}
};
</script>

<style lang="scss" scoped>
@import "../assets/scss/GlobalComponents";

.date{
    span{
        margin-left: $default-distance;
    }
} 

.temperature {
	::after {
		content: "\00b0";
	}
	p {
		font-weight: bold;
	}
}
</style>