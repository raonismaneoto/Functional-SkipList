import React, { useState, Fragment } from 'react'
import Input from '@vtex/styleguide/lib/Input'
import IconPlusLines from '@vtex/styleguide/lib/icon/PlusLines'
import IconGrid from '@vtex/styleguide/lib/icon/Grid'
import IconDelete from '@vtex/styleguide/lib/icon/Delete'
import IconCheck from '@vtex/styleguide/lib/icon/Check'
import ButtonWithIcon from '@vtex/styleguide/lib/ButtonWithIcon'
import Layout from '@vtex/styleguide/lib/Layout'
import Modal from '@vtex/styleguide/lib/Modal'
import PageBlock from '@vtex/styleguide/lib/PageBlock'
import PageHeader from '@vtex/styleguide/lib/PageHeader'

import View from './View'
import SkipList from './SkipList'

const Form = () => {
  const [skipList, setSkipList] = useState(undefined)
  const [value, setValue] = useState('')
  const [touched, setTouched] = useState(false)
  const [showModal, setShowModal] = useState(true)
  const [isLoading, setLoading] = useState(false)
  const [maxHeight, setMaxHeight] = useState('')
  const [foo, setFoo] = useState(false)

  const handleChange = e => {
    e.preventDefault()

    setTouched(true)
    setValue(e.target.value)
  }

  const handleCreate = e => {
    e.preventDefault()

    setLoading(true)
    setTimeout(() => {
      setSkipList(new SkipList(parseInt(maxHeight)))
      setLoading(false)
      setShowModal(false)
    }, 2000)
  }

  const handleInsert = e => {
    if (skipList) {
      setLoading(true)
      const iValue = parseInt(value)
      setTimeout(() => {
        if (!skipList.contains(iValue)) {
          skipList.insert(iValue)
          setSkipList(skipList)
        }
        setLoading(false)
      }, 2000)
    }
  }

  const handleDelete = e => {
    if (skipList) {
      const iValue = parseInt(value)
      setFoo(true)
      setTimeout(() => {
        if (skipList.contains(iValue)) {
          skipList.remove(iValue)
          setSkipList(skipList)
        }
        setFoo(false)
      }, 2000)
    }
  }

  return (
    <div className="flex flex-column vh-100">
      {showModal && (
        <Modal centered isOpen={showModal} onClose={() => setShowModal(false)}>
          <div className="flex flex-column">
            <span className="t-heading-5 mb7">Create Skip List</span>
            <div className="flex">
              <div className="w-50 mr4">
                <Input
                  label="Maximum height"
                  placeholder="Insert the maximum height"
                  type="number"
                  value={maxHeight}
                  onChange={e => setMaxHeight(e.target.value)}
                />
              </div>
              <div className="w-50">
                <Input
                  label="Probability"
                  placeholder="Insert the probability"
                  type="number"
                />
              </div>
            </div>
            <div className="flex justify-end mt4">
              <ButtonWithIcon
                icon={<IconCheck />}
                onClick={handleCreate}
                isLoading={isLoading}>
                Create
              </ButtonWithIcon>
            </div>
          </div>
        </Modal>
      )}
      <Layout
        pageHeader={
          <PageHeader title="Skip List">
            <ButtonWithIcon
              icon={<IconGrid />}
              variation="secondary"
              onClick={() => setShowModal(true)}>
              New Skip List
            </ButtonWithIcon>
          </PageHeader>
        }>
        <Fragment>
          <PageBlock>
            <div className="flex flex-column items-center">
              <div className="w-50 mb4">
                <Input
                  label="Value"
                  placeholder="Insert the value"
                  value={value}
                  type="number"
                  onChange={handleChange}
                  errorMessage={
                    touched && !value.length && 'The value cannot be empty'
                  }
                />
              </div>
              <div className="flex items-center w-50 mb5">
                <div className="w-50 mr4">
                  <ButtonWithIcon
                    block
                    isLoading={isLoading}
                    icon={<IconPlusLines />}
                    onClick={handleInsert}>
                    Insert
                  </ButtonWithIcon>
                </div>
                <div className="w-50">
                  <ButtonWithIcon
                    block
                    isLoading={foo}
                    variation="danger"
                    icon={<IconDelete />}
                    onClick={handleDelete}>
                    Remove
                  </ButtonWithIcon>
                </div>
              </div>
            </div>
          </PageBlock>
          <View skipList={skipList} />
        </Fragment>
      </Layout>
    </div>
  )
}

export default Form
