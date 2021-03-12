Pod::Spec.new do |spec|
    spec.name                     = 'News'
    spec.version                  = '0.1.0'
    spec.homepage                 = '..'
    spec.source                   = { :git => "..." }
    spec.authors                  = 'IceRock Development'
    spec.license                  = ''
    spec.summary                  = '...'

    spec.module_name              = "#{spec.name}"

    spec.ios.deployment_target  = '11.0'
    spec.swift_version = '5'

    spec.source_files = "Classes/**/*.{h,m,swift}"
end