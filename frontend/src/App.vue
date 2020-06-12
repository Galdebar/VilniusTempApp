<template>
	<div id="app" class="app-wrapper container">
		<div class="fixed">
			<Header />
			<CurrentTemp />
		</div>

		<TempHistory />
	</div>
</template>

<script>
import Header from "./components/Header";
import CurrentTemp from "./components/CurrentTemp";
import TempHistory from "./components/TempHistory";

export default {
	name: "App",
	components: {
		Header,
		CurrentTemp,
		TempHistory
	},
	mounted() {
		this.$store.dispatch("updateTemp");
		this.$store.dispatch("updateHistory", 1);
		this.$store.dispatch("startRefresh");
	}
};
</script>

<style lang="scss" scoped>
@import "assets/scss/GlobalComponents";
@import "assets/scss/Variables";

#app {
	height: 100%;

	.container {
		margin-top: $default-distance;
		margin-bottom: $large-distance;
		position: relative;
		box-sizing: border-box;
	}

	.fixed {
		position: fixed;
		box-sizing: border-box;
		width: $wrapper-width-desktop;
	}
}
</style>
