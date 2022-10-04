const TimelineTableFields = [

	{
		label: 'Den',
		key: 'fromTime',
		format: {
			type: 'date',
			pattern: 'dddd'
		}

	},
	{
		label: 'Datum',
		key: 'fromTime',
		format: {
			type: 'date',
			pattern: 'DD.MM'
		},
		thClass: 'w120'
	},
	{
		label: 'Od',
		key: 'fromTime',
		format: {
			type: 'date',
			pattern: 'HH:mm'
		}
	},
	{
		label: 'Do',
		key: 'toTime',
		format: {
			type: 'date',
			pattern: 'HH:mm'
		}
	},
	{
		label: 'Pauza',
		key: 'pause',
		thClass: 'w80',
		tdClass: 'text-align-right'
	},
	{
		label: 'Odpracováno',
		key: 'workTime',
		thClass: 'w80'
	},
	{
		label: 'Projekt',
		key: 'projectName',
		filter: {
			comparator: 'like'
		}
	},
	{
		label: 'Poznámka',
		key: 'note',
		filter: {
			comparator: 'like'
		}
	},
	{
		label: 'Vzdálený zápis',
		key: 'remoteWriters',
		thClass: 'w260'
	},
	{
		label: '#',
		key: 'NotExistingKey'
	}
]

export default TimelineTableFields
