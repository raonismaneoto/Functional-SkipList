import React, { Fragment } from 'react'
import PageBlock from '@vtex/styleguide/lib/PageBlock'
import ArrowBack from '@vtex/styleguide/lib/icon/ArrowBack'

const View = ({ skipList }) => {
  const columns = () => {
    if (skipList) {
      let levels = []
      const x = skipList.toList()
      console.log(x)
      for (let i = 1; i <= skipList.getSize(); ++i) {
        const bar = x[skipList.getHeight()][i]
        let foo = []
        for (let j = skipList.getHeight() - 1; j >= 0; --j) {
          if (x[j].includes(bar)) {
            foo.push(bar)
          } else {
            foo.push(undefined)
          }
        }
        console.log(foo)
        levels.push(foo)
      }
      return levels
    }
    return []
  }

  const render = () => {
    const c = columns()
    return (
      <div className="flex overflow-x-scroll">
        {c.map((foo, index) => {
          let bar = foo.reverse()
          return (
            <div className="flex flex-column mh4">
              {bar.map(num => (
                <div className="flex">
                  <div
                    className={`flex items-center justify-center h3 w3 br3 b--disabled mb4 ${num !==
                      undefined && 'ba'}`}>
                    {num}
                  </div>
                  <div className="flex items-center justify-center h3 w3 br3 b--disabled ml4 mb4 rotate-180">
                    {num !== undefined && index + 1 !== c.length && (
                      <ArrowBack />
                    )}
                  </div>
                </div>
              ))}
            </div>
          )
        })}
      </div>
    )
  }

  return (
    <PageBlock>
      <div className="flex justify-center">
        {skipList && skipList.getSize() > 0 ? (
          render()
        ) : (
          <div className="w-100 tc t-heading-5 c-muted-2">
            The Skip List is empty
          </div>
        )}
      </div>
    </PageBlock>
  )
}

export default View
