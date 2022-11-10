const formatWorkTime = (timeInMin: number):string => {
    return Math.trunc((timeInMin/60)).toString(10).padStart(2, '0')  +':'+(timeInMin%60).toString(10).padStart(2, '0')
}

const formatPrice = (input: number): string => {
    return input.toFixed(2) + ' Kč'
}

export {formatWorkTime, formatPrice}
