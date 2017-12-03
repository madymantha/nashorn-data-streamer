
function Stats() {
};

Stats.prototype.miles = 0;
Stats.prototype.calories = 0;

Stats.prototype.addMiles = function(strMiles) {
   this.miles += Number(strMiles);
};

Stats.prototype.addCalories = function(strCalories) {
   this.calories += Number(strCalories);
};

Stats.prototype.print = function() {
    print('   Miles: ' + this.miles);
    print('Calories: ' + this.calories);
};

var walkingStats;
var runningStats;

function onBegin() {
    walkingStats = new Stats();
    runningStats = new Stats();
}

function onRecord(record) {
    if (record.activity === 'Running') {
        runningStats.addMiles(record.miles);
        runningStats.addCalories(record.calories);

    } else {
        walkingStats.addMiles(record.miles);
        walkingStats.addCalories(record.calories);
    }
}

function onEnd() {
    print('----- Walking -----');
    walkingStats.print();

    print('');
    print('----- Running -----');
    runningStats.print();
}
