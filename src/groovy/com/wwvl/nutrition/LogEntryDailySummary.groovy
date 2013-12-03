package com.wwvl.nutrition

class LogEntryDailySummary {
	Date dateEaten
	Integer calories = 0
	BigDecimal fiveDayAvg
	BigDecimal tenDayAvg

	static def computeAveragesFromSet(Date startDate,Date endDate, def set){
		def interval = endDate - startDate - 1
		def result = new LinkedHashMap<LogEntryDailySummary>()

		//build a logEntry for each day in the range
		(0..interval).each{
			def summary = new LogEntryDailySummary()
			summary.dateEaten =startDate + it
			result[summary.dateEaten.format('MM/dd/yyyy')] = summary
		}

		//insert summed calorie counts into our range
		set.each{result[it[1].format("MM/dd/yyyy")].calories = it[0]}

		def RollingFive = 0
		def Rollingten = 0
		LinkedList<Integer> fiveDayWindow = new LinkedList<Integer>();
		LinkedList<Integer> tenDayWindow = new LinkedList<Integer>();

		(0..interval).each{
			def date = startDate + it
			def summary = result[date.format('MM/dd/yyyy')]
			fiveDayWindow.add(summary.calories)
			RollingFive += summary.calories
			if(fiveDayWindow.size() == 6){
				RollingFive -= fiveDayWindow.removeFirst()
			}
			summary.fiveDayAvg = RollingFive / Math.min(5,fiveDayWindow.size())

			tenDayWindow.add(summary.calories)
			Rollingten += summary.calories
			if(tenDayWindow.size() == 11){
				Rollingten -= tenDayWindow.removeFirst()
			}
			summary.tenDayAvg = Rollingten / Math.min(10,tenDayWindow.size())
		}

		result

	}
}
