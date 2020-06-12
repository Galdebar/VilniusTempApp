<template>
	<div class="history-container">
		<div>
			<h2>History</h2>
			<transition-group name="list-complete">
				<HistoryItem
					v-for="item in historyItems"
					v-bind:temperature-item="item"
					v-bind:key="item.observationTime"
				/>
			</transition-group>
			<button v-on:click="loadFullHistory">Load Full History</button>
		</div>
	</div>
</template>

<script>
import HistoryItem from "./HistoryItem";

export default {
	name: "TempHistory",
	components: {
		HistoryItem
	},
	computed: {
		historyItems: function() {
			return this.$store.getters.getHistory;
		}
	},
	methods: {
		loadFullHistory() {
			this.$store.dispatch("updateHistory");
		}
	}
};
</script>

<style lang="scss" scoped>
@import "../assets/scss/GlobalComponents";

.history-container {
	margin-top: $extra-large-distance * 4;

	button {
		margin: $default-distance auto;
		width: 100%;
	}
}

h2 {
	text-align: center;
	margin-top: $default-distance;
}
</style>