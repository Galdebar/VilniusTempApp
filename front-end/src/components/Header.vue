<template>
	<header>
		<div class="logo">
			<div class="title">
				<h1>Vilnius Temp</h1>
			</div>
		</div>
	</header>
</template>

<script>
export default {
	name: "Header"
};
</script>

<style lang="scss" scoped>
@import "../assets/scss/Variables";

$shop-talk: #f26906;
$background: #000;

html {
	background: $brand-color;
	color: $brand-color;
	font-size: 6px;
}

.title {
	background: $brand-color;
    border-top: 0.8rem solid $brand-color;
    border-top-right-radius: $corner-rounding;
    border-top-left-radius: $corner-rounding;
	border-bottom: 0.8rem solid $brand-color;
    font-family: "Montserrat", sans-serif;
    color:$default-black;
	font-size: 2.5em;
	font-weight: 700;
	line-height: 1;
	text-align: center;
	text-transform: uppercase;
}

h1 {
	border-top: 0.4rem solid $default-black;
	border-bottom: 0.4rem solid $default-black;
	margin: 0 -40px;
	padding: 0;
	transform: scale(0.8, 1);
}

h3 {
	color: $default-black;
	font-size: 40px;
	text-align: center;
	margin-bottom: 30px;
}
</style>