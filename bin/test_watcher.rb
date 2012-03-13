watch ".*" do |filename|
  system "clear"
  system "lein test"
end
