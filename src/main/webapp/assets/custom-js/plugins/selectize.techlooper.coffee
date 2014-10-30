Selectize.define "techlooper", (options) ->
  self = this
  triggeredOn = {}
  onReturn = options.onReturn

  self.focusNoDropdown = (->
    ->
      triggeredOn.focusNoDropdown = true
      self.focus())()

  self.open = (->
    original = self.open
    ->
      return triggeredOn.onOptionSelect = false if triggeredOn.onOptionSelect
      return triggeredOn.focusNoDropdown = false if triggeredOn.focusNoDropdown
      fn = original.apply(this, arguments)
      fn)()

  self.onOptionSelect = ((e) ->
    original = self.onOptionSelect
    (e) ->
      fn = original.apply(this, arguments)
      self.close()
      triggeredOn.onOptionSelect = true
      fn)()


  self.onKeyDown = ((e) ->
    original = self.onKeyDown
    (e) ->
      dropdownOpened = false
      switch e.keyCode
        when 13 then dropdownOpened = self.isOpen
        when 27 then e.preventDefault() if self.isOpen

      fn = original.apply(this, arguments)

      switch e.keyCode
        when 13 then onReturn(e) if not dropdownOpened and $.type(onReturn) is "function"

      fn)()
