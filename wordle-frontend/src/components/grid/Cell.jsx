import classnames from 'classnames'
import { useEffect,useRef, useState } from 'react';

import {REVEAL_TIME_MS} from '../../constants/settings'
import {getStoredIsHighContrastMode} from '../../lib/localStorage'

export const Cell = ({
                         value,
                         status,
                         isRevealing,
                         isCompleted,
                         position = 0,
                     }) => {
    const isFilled = value && !isCompleted
    const shouldReveal = isRevealing && isCompleted
    const animationDelay = `${position * REVEAL_TIME_MS}ms`
    const isHighContrast = getStoredIsHighContrastMode()

    const letterContainerRef = useRef(null);
    // State to control the addition of 'rendered' class
    const [isRendered, setIsRendered] = useState(false);

    const classes = classnames(
        'xxshort:w-11 xxshort:h-11 short:text-2xl short:w-12 short:h-12 w-14 h-14 border-solid border-2 flex items-center justify-center mx-0.5 text-4xl font-bold rounded dark:text-white',
        {
            'bg-white dark:bg-slate-900 border-slate-200 dark:border-slate-600':
                !status,
            'border-black dark:border-slate-100': value && !status,
            'absent shadowed bg-slate-400 dark:bg-slate-700 text-white border-slate-400 dark:border-slate-700':
                status === 'absent',
            'correct shadowed bg-orange-500 text-white border-orange-500':
                status === 'correct' && isHighContrast,
            'present shadowed bg-cyan-500 text-white border-cyan-500':
                status === 'present' && isHighContrast,
            'correct shadowed bg-green-500 text-white border-green-500':
                status === 'correct' && !isHighContrast,
            'present shadowed bg-yellow-500 text-white border-yellow-500':
                status === 'present' && !isHighContrast,
            'cell-fill-animation': isFilled,
            'cell-reveal': shouldReveal,
        }
    )

    useEffect(() => {
        if (shouldReveal) {
            const delay = position * REVEAL_TIME_MS;
            const animationDuration = 200; // you need to define this constant
            const totalDuration = delay + animationDuration;

            const timer = setTimeout(() => {
                setIsRendered(true);
            }, totalDuration);

            return () => clearTimeout(timer);
        }
    }, [position, shouldReveal]);

    // Add 'rendered' to classes if isRendered is true
    const letterContainerClasses = classnames('letter-container', {
        rendered: isRendered,
    });

    return (
        <div className={classes} style={{animationDelay}}>
            <div ref={letterContainerRef} className={letterContainerClasses} style={{animationDelay}}>
                {value}
            </div>
        </div>
    )
}
